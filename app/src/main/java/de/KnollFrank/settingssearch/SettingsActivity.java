package de.KnollFrank.settingssearch;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.MergedPreferenceScreen;
import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.client.SearchConfiguration;
import de.KnollFrank.lib.settingssearch.client.SearchPreferenceFragments;
import de.KnollFrank.lib.settingssearch.common.SearchablePreferences;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.fragment.data.PreferencePathNavigatorData;
import de.KnollFrank.lib.settingssearch.results.recyclerview.FragmentContainerViewAdder;
import de.KnollFrank.settingssearch.preference.fragment.PrefsFragmentFirst;

public class SettingsActivity extends AppCompatActivity {

    private static final @IdRes int FRAGMENT_CONTAINER_VIEW_ID = R.id.settings;
    private static final @IdRes int DUMMY_FRAGMENT_CONTAINER_VIEW = View.generateViewId();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            this
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(FRAGMENT_CONTAINER_VIEW_ID, new SettingsFragment())
                    .commit();
        }
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        this
                .getPreferencePathNavigatorData()
                .ifPresent(this::showPreferenceScreenAndHighlightPreference);
    }

    private Optional<PreferencePathNavigatorData> getPreferencePathNavigatorData() {
        return Optional
                .ofNullable(getIntent().getExtras())
                .map(PreferencePathNavigatorData::fromBundle);
    }

    private void showPreferenceScreenAndHighlightPreference(final PreferencePathNavigatorData preferencePathNavigatorData) {
        FragmentContainerViewAdder.addInvisibleFragmentContainerViewWithIdToParent(
                findViewById(R.id.settings_root),
                DUMMY_FRAGMENT_CONTAINER_VIEW);
        final MergedPreferenceScreen mergedPreferenceScreen =
                createSearchPreferenceFragments()
                        .getMergedPreferenceScreenFactory()
                        .getMergedPreferenceScreen(
                                getSupportFragmentManager(),
                                // FK-TODO: use a real childFragmentManager:
                                getSupportFragmentManager(),
                                progress -> {
                                },
                                DUMMY_FRAGMENT_CONTAINER_VIEW);
        final PreferencePath preferencePath =
                new PreferencePath(
                        convertIds2Preferences(
                                preferencePathNavigatorData.preferencePathIds(),
                                mergedPreferenceScreen.preferences()));
        final SearchablePreference searchablePreference = preferencePath.getPreference().orElseThrow();
        correctPreferencePath(
                searchablePreference,
                // FK-FIXME: INDEX_WITHIN_PREFERENCE_PATH ist falsch, falls über mehrere Activities hinweg navigiert wird.
                preferencePathNavigatorData.indexWithinPreferencePath());
        mergedPreferenceScreen
                .searchResultsDisplayer()
                .getSearchResultsFragment()
                .showPreferenceScreenAndHighlightPreference
                .showPreferenceScreenAndHighlightPreference(searchablePreference);
    }

    private void correctPreferencePath(final SearchablePreference searchablePreference,
                                       final int indexWithinPreferencePath) {
        searchablePreference.setPreferencePath(
                getCorrectedPreferencePath(
                        searchablePreference.getPreferencePath(),
                        indexWithinPreferencePath));
    }

    private PreferencePath getCorrectedPreferencePath(final PreferencePath preferencePath,
                                                      final int indexWithinPreferencePath) {
        final List<SearchablePreference> preferences = preferencePath.preferences();
        return new PreferencePath(preferences.subList(indexWithinPreferencePath, preferences.size()));
    }

    private static List<SearchablePreference> convertIds2Preferences(final List<Integer> preferencePathIds, final Set<SearchablePreference> preferences) {
        return preferencePathIds
                .stream()
                .map(getSearchablePreferenceById(preferences)::get)
                .collect(Collectors.toList());
    }

    private static Map<Integer, SearchablePreference> getSearchablePreferenceById(final Set<SearchablePreference> preferences) {
        return SearchablePreferences
                .getPreferencesRecursively(preferences)
                .stream()
                .collect(
                        Collectors.toMap(
                                SearchablePreference::getId,
                                Function.identity()));
    }

    private SearchPreferenceFragments createSearchPreferenceFragments() {
        return SearchPreferenceFragmentsFactory.createSearchPreferenceFragments(
                createSearchConfiguration(PrefsFragmentFirst.class),
                getSupportFragmentManager(),
                this,
                Optional::empty);
    }

    private SearchConfiguration createSearchConfiguration(final Class<? extends PreferenceFragmentCompat> rootPreferenceFragment) {
        return new SearchConfiguration(
                FRAGMENT_CONTAINER_VIEW_ID,
                Optional.empty(),
                rootPreferenceFragment);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }
}