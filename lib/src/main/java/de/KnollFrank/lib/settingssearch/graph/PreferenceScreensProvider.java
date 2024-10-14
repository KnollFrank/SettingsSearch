package de.KnollFrank.lib.settingssearch.graph;

import androidx.annotation.RawRes;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.ConnectedSearchablePreferenceScreens;
import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostClass;

public class PreferenceScreensProvider {

    private final SearchablePreferenceScreenGraphDAOProvider searchablePreferenceScreenGraphDAOProvider;

    public PreferenceScreensProvider(final SearchablePreferenceScreenGraphDAOProvider searchablePreferenceScreenGraphDAOProvider) {
        this.searchablePreferenceScreenGraphDAOProvider = searchablePreferenceScreenGraphDAOProvider;
    }

    public ConnectedSearchablePreferenceScreens getConnectedPreferenceScreens(final String rootPreferenceFragmentClassName,
                                                                              final SearchablePreferenceScreenGraphDAOProvider.Mode mode,
                                                                              final @RawRes int searchablePreferenceScreenGraph) {
        final Graph<PreferenceScreenWithHostClass, PreferenceEdge> _searchablePreferenceScreenGraph =
                searchablePreferenceScreenGraphDAOProvider.getSearchablePreferenceScreenGraph(rootPreferenceFragmentClassName, mode, searchablePreferenceScreenGraph);
        return ConnectedSearchablePreferenceScreens.fromSearchablePreferenceScreenGraph(_searchablePreferenceScreenGraph);
    }
}
