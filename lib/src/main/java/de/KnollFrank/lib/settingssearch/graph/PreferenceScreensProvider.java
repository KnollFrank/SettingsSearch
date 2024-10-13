package de.KnollFrank.lib.settingssearch.graph;

import androidx.annotation.RawRes;
import androidx.preference.PreferenceManager;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.ConnectedSearchablePreferenceScreens;
import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostClass;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.db.SearchableInfoAndDialogInfoProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceConnected2PreferenceFragmentProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceScreenGraphAvailableListener;

public class PreferenceScreensProvider {

    private final SearchablePreferenceScreenGraphDAOProvider searchablePreferenceScreenGraphDAOProvider;

    public PreferenceScreensProvider(final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider,
                                     final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider,
                                     final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener,
                                     final SearchableInfoAndDialogInfoProvider searchableInfoAndDialogInfoProvider,
                                     final PreferenceManager preferenceManager) {
        final SearchablePreferenceScreenGraphProvider searchablePreferenceScreenGraphProvider =
                new SearchablePreferenceScreenGraphProvider(
                        preferenceScreenWithHostProvider,
                        preferenceConnected2PreferenceFragmentProvider,
                        preferenceScreenGraphAvailableListener,
                        searchableInfoAndDialogInfoProvider);
        // FK-TODO: SearchablePreferenceScreenGraphDAOProvider direkt im Konstruktor übergeben
        this.searchablePreferenceScreenGraphDAOProvider = new SearchablePreferenceScreenGraphDAOProvider(searchablePreferenceScreenGraphProvider, preferenceManager);
    }

    public ConnectedSearchablePreferenceScreens getConnectedPreferenceScreens(final String rootPreferenceFragmentClassName,
                                                                              final SearchablePreferenceScreenGraphDAOProvider.Mode mode,
                                                                              final @RawRes int searchablePreferenceScreenGraph) {
        final Graph<PreferenceScreenWithHostClass, PreferenceEdge> _searchablePreferenceScreenGraph =
                searchablePreferenceScreenGraphDAOProvider.getSearchablePreferenceScreenGraph(rootPreferenceFragmentClassName, mode, searchablePreferenceScreenGraph);
        return ConnectedSearchablePreferenceScreens.fromSearchablePreferenceScreenGraph(_searchablePreferenceScreenGraph);
    }
}
