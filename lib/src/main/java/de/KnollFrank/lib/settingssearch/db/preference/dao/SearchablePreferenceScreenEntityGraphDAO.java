package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreens2GraphConverter.convertScreensToGraph;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProviderFactory;
import de.KnollFrank.lib.settingssearch.graph.EntityGraphAndDbDataProvider;

public class SearchablePreferenceScreenEntityGraphDAO {

    private final SearchablePreferenceScreenEntityDAO searchablePreferenceScreenDAO;
    private final SearchablePreferenceEntityDAO searchablePreferenceDAO;

    public SearchablePreferenceScreenEntityGraphDAO(final SearchablePreferenceScreenEntityDAO searchablePreferenceScreenDAO,
                                                    final SearchablePreferenceEntityDAO searchablePreferenceDAO) {
        this.searchablePreferenceScreenDAO = searchablePreferenceScreenDAO;
        this.searchablePreferenceDAO = searchablePreferenceDAO;
    }

    public void persist(final EntityGraphAndDbDataProvider entityGraph) {
        removePersistedGraph();
        searchablePreferenceScreenDAO.persist(entityGraph.entityGraph().vertexSet(), entityGraph.dbDataProvider());
    }

    public EntityGraphAndDbDataProvider load() {
        final DbDataProvider dbDataProvider = getDbDataProvider();
        return new EntityGraphAndDbDataProvider(
                convertScreensToGraph(
                        searchablePreferenceScreenDAO.loadAll(),
                        dbDataProvider),
                dbDataProvider);
    }

    private void removePersistedGraph() {
        searchablePreferenceScreenDAO.removeAll();
    }

    private DbDataProvider getDbDataProvider() {
        return DbDataProviderFactory.createDbDataProvider(
                searchablePreferenceScreenDAO,
                searchablePreferenceDAO);
    }
}
