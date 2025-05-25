package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreens2GraphConverter.convertScreensToGraph;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

// FK-TODO: make this class a @Dao and introduce a new @Entity named SearchablePreferenceScreenGraph which references it's root SearchablePreferenceScreen. Then remove SearchDatabaseStateDAO and SearchDatabaseState.
public class SearchablePreferenceScreenGraphDAO {

    private final SearchablePreferenceScreenDAO searchablePreferenceScreenDAO;

    public SearchablePreferenceScreenGraphDAO(final SearchablePreferenceScreenDAO searchablePreferenceScreenDAO) {
        this.searchablePreferenceScreenDAO = searchablePreferenceScreenDAO;
    }

    public void persist(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph) {
        detachGraphFromDb(graph);
        removePersistedGraph();
        searchablePreferenceScreenDAO.persist(graph.vertexSet());
    }

    public Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> load() {
        return convertScreensToGraph(searchablePreferenceScreenDAO.loadAll());
    }

    private void removePersistedGraph() {
        searchablePreferenceScreenDAO.removeAll();
    }

    // FK-TODO: rename method and move to SearchablePreferenceScreenDAO
    private void detachGraphFromDb(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph) {
        SearchablePreferenceScreenGraphDAO
                .createSearchablePreferenceScreenDAOSetter(searchablePreferenceScreenDAO.createDetachedDbDataProvider())
                .setDao(graph.vertexSet());
    }

    private static SearchablePreferenceScreenDAOSetter createSearchablePreferenceScreenDAOSetter(final DbDataProvider detachedDbDataProvider) {
        return new SearchablePreferenceScreenDAOSetter(
                detachedDbDataProvider,
                new SearchablePreferenceDAOSetter(detachedDbDataProvider));
    }
}
