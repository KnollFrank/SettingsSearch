package de.KnollFrank.lib.settingssearch.db.preference.dao;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.graph.EntityGraph2PojoGraphTransformer;
import de.KnollFrank.lib.settingssearch.graph.PojoGraph2EntityGraphTransformer;

// FK-TODO: make this class a @Dao and introduce a new @Entity named SearchablePreferenceScreenGraph which references it's root SearchablePreferenceScreen. Then remove SearchDatabaseStateDAO and SearchDatabaseState.
public class SearchablePreferenceScreenGraphDAO {

    private final SearchablePreferenceScreenEntityGraphDAO entityGraphDAO;

    public SearchablePreferenceScreenGraphDAO(final SearchablePreferenceScreenEntityGraphDAO entityGraphDAO) {
        this.entityGraphDAO = entityGraphDAO;
    }

    public void persist(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph) {
        entityGraphDAO.persist(PojoGraph2EntityGraphTransformer.toEntityGraph(graph));
    }

    public Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> load() {
        // FK-TODO: cache persisted and loaded graph?
        final var entityGraphAndDbDataProvider = entityGraphDAO.load();
        return EntityGraph2PojoGraphTransformer.toPojoGraph(
                entityGraphAndDbDataProvider.entityGraph(),
                entityGraphAndDbDataProvider.dbDataProvider());
    }
}
