package de.KnollFrank.lib.settingssearch;

import androidx.preference.PreferenceManager;

import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.MergedPreferenceScreenData;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactoryAndInitializer;
import de.KnollFrank.lib.settingssearch.fragment.PreferencePathNavigator;
import de.KnollFrank.lib.settingssearch.provider.PreferencePathByPreference;
import de.KnollFrank.lib.settingssearch.results.SearchResultsDisplayer;
import de.KnollFrank.lib.settingssearch.results.SearchResultsDisplayerFactory;

public record MergedPreferenceScreen(
        Set<SearchablePreferencePOJO> allPreferencesForSearch,
        SearchResultsDisplayer searchResultsDisplayer,
        PreferencePathNavigator preferencePathNavigator) {

    public static MergedPreferenceScreen of(
            final MergedPreferenceScreenData mergedPreferenceScreenData,
            final PreferenceManager preferenceManager,
            final FragmentFactoryAndInitializer fragmentFactoryAndInitializer) {
        return new MergedPreferenceScreen(
                mergedPreferenceScreenData.allPreferencesForSearch(),
                SearchResultsDisplayerFactory.createSearchResultsDisplayer(
                        preferenceManager,
                        pojoEntityMap ->
                                PreferencePathByPreference.getPreferencePathByPreference(
                                        pojoEntityMap,
                                        mergedPreferenceScreenData.preferencePathByPreference())),
                new PreferencePathNavigator(
                        mergedPreferenceScreenData.hostByPreference(),
                        fragmentFactoryAndInitializer,
                        preferenceManager.getContext()));
    }
}
