package de.KnollFrank.lib.settingssearch;

import androidx.preference.PreferenceManager;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.MergedPreferenceScreenData;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactoryAndInitializer;
import de.KnollFrank.lib.settingssearch.fragment.PreferencePathNavigator;
import de.KnollFrank.lib.settingssearch.provider.PreferencePathByPreference;
import de.KnollFrank.lib.settingssearch.results.SearchResultsDisplayerFactory;

public class MergedPreferenceScreenFactory {

    public static MergedPreferenceScreen getMergedPreferenceScreen(
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
