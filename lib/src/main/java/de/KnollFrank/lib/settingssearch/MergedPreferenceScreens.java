package de.KnollFrank.lib.settingssearch;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceManager;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.MergedPreferenceScreenData;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactoryAndInitializer;
import de.KnollFrank.lib.settingssearch.fragment.PreferencePathNavigator;
import de.KnollFrank.lib.settingssearch.provider.PrepareShow;
import de.KnollFrank.lib.settingssearch.provider.ShowPreferencePathPredicate;
import de.KnollFrank.lib.settingssearch.results.SearchResultsDisplayerFactory;
import de.KnollFrank.lib.settingssearch.results.ShowPreferenceScreenAndHighlightPreference;
import de.KnollFrank.lib.settingssearch.results.recyclerview.SearchResultsFragment;

public class MergedPreferenceScreens {

    public static MergedPreferenceScreen createMergedPreferenceScreen(
            final @IdRes int fragmentContainerViewId,
            final PrepareShow prepareShow,
            final ShowPreferencePathPredicate showPreferencePathPredicate,
            final FragmentManager fragmentManager,
            final MergedPreferenceScreenData mergedPreferenceScreenData,
            final PreferenceManager preferenceManager,
            final FragmentFactoryAndInitializer fragmentFactoryAndInitializer) {
        final PreferencePathNavigator preferencePathNavigator =
                new PreferencePathNavigator(
                        mergedPreferenceScreenData.hostByPreference(),
                        fragmentFactoryAndInitializer,
                        preferenceManager.getContext());
        return new MergedPreferenceScreen(
                mergedPreferenceScreenData.preferencePathByPreference(),
                mergedPreferenceScreenData.preferences(),
                SearchResultsDisplayerFactory.createSearchResultsDisplayer(
                        new SearchResultsFragment(
                                mergedPreferenceScreenData.preferencePathByPreference(),
                                new ShowPreferenceScreenAndHighlightPreference(
                                        preferencePathNavigator,
                                        mergedPreferenceScreenData.preferencePathByPreference(),
                                        fragmentContainerViewId,
                                        prepareShow,
                                        fragmentManager),
                                showPreferencePathPredicate),
                        preferenceManager),
                preferencePathNavigator,
                mergedPreferenceScreenData.hostByPreference());
    }
}
