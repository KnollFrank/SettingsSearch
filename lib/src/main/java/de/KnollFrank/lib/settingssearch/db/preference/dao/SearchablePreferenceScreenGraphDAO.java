package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreens2GraphConverter.convertScreensToGraph;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProviderFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntityEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;
import de.KnollFrank.lib.settingssearch.graph.EntityGraph2PojoGraphTransformer;
import de.KnollFrank.lib.settingssearch.graph.EntityGraphAndDbDataProvider;
import de.KnollFrank.lib.settingssearch.graph.PojoGraph2EntityGraphTransformer;

// FK-TODO: make this class a @Dao and introduce a new @Entity named SearchablePreferenceScreenGraph which references it's root SearchablePreferenceScreen. Then remove SearchDatabaseStateDAO and SearchDatabaseState.
public class SearchablePreferenceScreenGraphDAO {

    private final SearchablePreferenceScreenEntityDAO searchablePreferenceScreenDAO;
    private final SearchablePreferenceEntityDAO searchablePreferenceDAO;

    public SearchablePreferenceScreenGraphDAO(final SearchablePreferenceScreenEntityDAO searchablePreferenceScreenDAO,
                                              final SearchablePreferenceEntityDAO searchablePreferenceDAO) {
        this.searchablePreferenceScreenDAO = searchablePreferenceScreenDAO;
        this.searchablePreferenceDAO = searchablePreferenceDAO;
    }

    public void persist(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph) {
        removePersistedGraph();
        final EntityGraphAndDbDataProvider entityGraph = PojoGraph2EntityGraphTransformer.toEntityGraph(graph);
        searchablePreferenceScreenDAO.persist(entityGraph.entityGraph().vertexSet(), entityGraph.dbDataProvider());
    }

    public Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> load() {
        // FK-TODO: cache persisted and loaded graph?
        final DbDataProvider dbDataProvider = getDbDataProvider();
        final Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> entityGraph =
                convertScreensToGraph(
                        searchablePreferenceScreenDAO.loadAll(),
                        dbDataProvider);
        return EntityGraph2PojoGraphTransformer.toPojoGraph(entityGraph, dbDataProvider);
    }

    public DbDataProvider getDbDataProvider() {
        return DbDataProviderFactory.createDbDataProvider(
                searchablePreferenceScreenDAO,
                searchablePreferenceDAO);
    }

    private void removePersistedGraph() {
        searchablePreferenceScreenDAO.removeAll();
    }
}
