package de.KnollFrank.settingssearch;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import de.KnollFrank.lib.settingssearch.MergedPreferenceScreen;
import de.KnollFrank.lib.settingssearch.client.SearchConfiguration;
import de.KnollFrank.lib.settingssearch.client.SearchPreferenceFragments;
import de.KnollFrank.lib.settingssearch.common.SearchablePreferences;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.fragment.PreferencePathNavigatorData;
import de.KnollFrank.settingssearch.preference.fragment.PrefsFragmentFirst;

class ContinueWithPreferencePathNavigation {

    private final FragmentActivity activity;
    private final @IdRes int fragmentContainerViewId;

    public ContinueWithPreferencePathNavigation(final FragmentActivity activity,
                                                final @IdRes int fragmentContainerViewId) {
        this.fragmentContainerViewId = fragmentContainerViewId;
        this.activity = activity;
    }

    public void onStart() {
        this
                .getPreferencePathNavigatorData()
                .ifPresent(this::showPreferenceScreenAndHighlightPreference);
    }

    private Optional<PreferencePathNavigatorData> getPreferencePathNavigatorData() {
        return Optional
                .ofNullable(activity.getIntent().getExtras())
                .map(PreferencePathNavigatorData::fromBundle);
    }

    private void showPreferenceScreenAndHighlightPreference(final PreferencePathNavigatorData preferencePathNavigatorData) {
        final SearchPreferenceFragments searchPreferenceFragments =
                createSearchPreferenceFragments(
                        mergedPreferenceScreen ->
                                mergedPreferenceScreen
                                        .searchResultsDisplayer()
                                        .getSearchResultsFragment()
                                        .showPreferenceScreenAndHighlightPreference
                                        .showPreferenceScreenAndHighlightPreference(
                                                getPreferenceFromId(
                                                        preferencePathNavigatorData.idOfSearchablePreference(),
                                                        mergedPreferenceScreen.preferences()),
                                                preferencePathNavigatorData.indexWithinPreferencePath()));
        searchPreferenceFragments.showSearchPreferenceFragment();
    }

    private SearchPreferenceFragments createSearchPreferenceFragments(final Consumer<MergedPreferenceScreen> onMergedPreferenceScreenAvailable) {
        return SearchPreferenceFragmentsFactory.createSearchPreferenceFragments(
                createSearchConfiguration(PrefsFragmentFirst.class),
                activity.getSupportFragmentManager(),
                activity,
                Optional::empty,
                onMergedPreferenceScreenAvailable);
    }

    private SearchConfiguration createSearchConfiguration(final Class<? extends PreferenceFragmentCompat> rootPreferenceFragment) {
        return new SearchConfiguration(
                fragmentContainerViewId,
                Optional.empty(),
                rootPreferenceFragment);
    }

    public static SearchablePreference getPreferenceFromId(final int id,
                                                           final Set<SearchablePreference> preferences) {
        return SearchablePreferences
                .getPreferencesRecursively(preferences)
                .stream()
                .filter(_preference -> _preference.getId() == id)
                .findFirst()
                .orElseThrow();
    }
}
