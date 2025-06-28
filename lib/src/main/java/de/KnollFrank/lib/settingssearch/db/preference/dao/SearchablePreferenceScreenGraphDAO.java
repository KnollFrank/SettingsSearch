package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreens2GraphConverter.convertScreensToGraph;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProviderFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
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
        final var entityGraph = PojoGraph2EntityGraphTransformer.toEntityGraph(graph);
        persist(entityGraph);
    }

    public Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> load() {
        // FK-TODO: cache persisted and loaded graph?
        final var entityGraphAndDbDataProvider = _load();
        return EntityGraph2PojoGraphTransformer.toPojoGraph(
                entityGraphAndDbDataProvider.entityGraph(),
                entityGraphAndDbDataProvider.dbDataProvider());
    }

    private void persist(final EntityGraphAndDbDataProvider entityGraph) {
        removePersistedGraph();
        searchablePreferenceScreenDAO.persist(entityGraph.entityGraph().vertexSet(), entityGraph.dbDataProvider());
    }

    private void removePersistedGraph() {
        searchablePreferenceScreenDAO.removeAll();
    }

    private EntityGraphAndDbDataProvider _load() {
        final DbDataProvider dbDataProvider = getDbDataProvider();
        return new EntityGraphAndDbDataProvider(
                convertScreensToGraph(
                        searchablePreferenceScreenDAO.loadAll(),
                        dbDataProvider),
                dbDataProvider);
    }

    private DbDataProvider getDbDataProvider() {
        return DbDataProviderFactory.createDbDataProvider(
                searchablePreferenceScreenDAO,
                searchablePreferenceDAO);
    }
}
