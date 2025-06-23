package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreens2GraphConverter.convertScreensToGraph;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProviderData;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProviderFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntityEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;

// FK-TODO: make this class a @Dao and introduce a new @Entity named SearchablePreferenceScreenGraph which references it's root SearchablePreferenceScreen. Then remove SearchDatabaseStateDAO and SearchDatabaseState.
public class SearchablePreferenceScreenGraphDAO {

    private final SearchablePreferenceScreenEntityDAO searchablePreferenceScreenDAO;
    private final SearchablePreferenceEntityDAO searchablePreferenceDAO;

    public SearchablePreferenceScreenGraphDAO(final SearchablePreferenceScreenEntityDAO searchablePreferenceScreenDAO,
                                              final SearchablePreferenceEntityDAO searchablePreferenceDAO) {
        this.searchablePreferenceScreenDAO = searchablePreferenceScreenDAO;
        this.searchablePreferenceDAO = searchablePreferenceDAO;
    }

    public void persist(final Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> graph) {
        final DbDataProviderData dbDataProviderData = searchablePreferenceScreenDAO.createDbDataProviderData();
        removePersistedGraph();
        searchablePreferenceScreenDAO.persist(graph.vertexSet(), DbDataProviderFactory.createDbDataProvider(dbDataProviderData));
    }

    public Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> load() {
        return convertScreensToGraph(
                searchablePreferenceScreenDAO.loadAll(),
                DbDataProviderFactory.createDbDataProvider(searchablePreferenceScreenDAO, searchablePreferenceDAO));
    }

    private void removePersistedGraph() {
        searchablePreferenceScreenDAO.removeAll();
    }
}
