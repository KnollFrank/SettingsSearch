package de.KnollFrank.lib.settingssearch;

import android.content.Context;

import androidx.preference.PreferenceManager;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.MergedPreferenceScreenData;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJOEdge;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactoryAndInitializer;
import de.KnollFrank.lib.settingssearch.fragment.PreferencePathNavigator;
import de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenGraphProvider;
import de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenGraphProviderWrapper;
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

    public static Graph<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> getSearchablePreferenceScreenGraph(
            final SearchablePreferenceScreenGraphProviderWrapper wrapper,
            final SearchablePreferenceScreenGraphProvider searchablePreferenceScreenGraphProvider,
            final Context context) {
        return wrapper
                .wrap(
                        searchablePreferenceScreenGraphProvider,
                        context)
                .getSearchablePreferenceScreenGraph();
    }
}
