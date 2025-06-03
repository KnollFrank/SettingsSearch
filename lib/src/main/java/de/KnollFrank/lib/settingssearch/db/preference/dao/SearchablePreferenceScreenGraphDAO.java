package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreens2GraphConverter.convertScreensToGraph;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProviders;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

// FK-TODO: make this class a @Dao and introduce a new @Entity named SearchablePreferenceScreenGraph which references it's root SearchablePreferenceScreen. Then remove SearchDatabaseStateDAO and SearchDatabaseState.
public class SearchablePreferenceScreenGraphDAO {

    private final SearchablePreferenceScreenDAO searchablePreferenceScreenDAO;
    private final SearchablePreferenceDAO searchablePreferenceDAO;

    public SearchablePreferenceScreenGraphDAO(final SearchablePreferenceScreenDAO searchablePreferenceScreenDAO,
                                              final SearchablePreferenceDAO searchablePreferenceDAO) {
        this.searchablePreferenceScreenDAO = searchablePreferenceScreenDAO;
        this.searchablePreferenceDAO = searchablePreferenceDAO;
    }

    public void persist(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph) {
        final SearchablePreferenceScreen.DbDataProvider dbDataProvider = searchablePreferenceScreenDAO.createDetachedDbDataProvider();
        removePersistedGraph();
        searchablePreferenceScreenDAO.persist(graph.vertexSet(), dbDataProvider);
    }

    public Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> load() {
        return convertScreensToGraph(
                searchablePreferenceScreenDAO.loadAll(),
                new DbDataProviders(
                        searchablePreferenceDAO,
                        searchablePreferenceScreenDAO));
    }

    private void removePersistedGraph() {
        searchablePreferenceScreenDAO.removeAll();
    }
}
