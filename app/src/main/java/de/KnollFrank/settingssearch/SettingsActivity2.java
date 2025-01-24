package de.KnollFrank.settingssearch;

import static de.KnollFrank.settingssearch.SettingsActivity.getPreferenceFromId;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.MergedPreferenceScreen;
import de.KnollFrank.lib.settingssearch.client.SearchConfiguration;
import de.KnollFrank.lib.settingssearch.client.SearchPreferenceFragments;
import de.KnollFrank.lib.settingssearch.fragment.PreferencePathNavigatorData;
import de.KnollFrank.lib.settingssearch.results.recyclerview.FragmentContainerViewAdder;
import de.KnollFrank.settingssearch.preference.fragment.PrefsFragmentFirst;

// FK-FIXME: search for signature2, click search result, press back button multiple times => stays at SettingsActivity2 but should go back.
public class SettingsActivity2 extends AppCompatActivity {

    private static final @IdRes int FRAGMENT_CONTAINER_VIEW_ID = R.id.settings;
    private static final @IdRes int DUMMY_FRAGMENT_CONTAINER_VIEW = View.generateViewId();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity2);
        if (savedInstanceState == null) {
            this
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(FRAGMENT_CONTAINER_VIEW_ID, new SettingsFragment2())
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
        mergedPreferenceScreen
                .searchResultsDisplayer()
                .getSearchResultsFragment()
                .showPreferenceScreenAndHighlightPreference
                .showPreferenceScreenAndHighlightPreference(
                        getPreferenceFromId(
                                preferencePathNavigatorData.idOfSearchablePreference(),
                                mergedPreferenceScreen.preferences()),
                        preferencePathNavigatorData.indexWithinPreferencePath());
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

    public static class SettingsFragment2 extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences2, rootKey);
        }
    }
}