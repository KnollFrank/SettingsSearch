package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreens2GraphConverter.convertScreensToGraph;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntityEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;

// FK-TODO: make this class a @Dao and introduce a new @Entity named SearchablePreferenceScreenGraph which references it's root SearchablePreferenceScreen. Then remove SearchDatabaseStateDAO and SearchDatabaseState.
public class SearchablePreferenceScreenGraphDAO {

    private final SearchablePreferenceScreenDAO searchablePreferenceScreenDAO;

    public SearchablePreferenceScreenGraphDAO(final SearchablePreferenceScreenDAO searchablePreferenceScreenDAO) {
        this.searchablePreferenceScreenDAO = searchablePreferenceScreenDAO;
    }

    public void persist(final Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> graph) {
        detachGraphFromDb(graph);
        removePersistedGraph();
        searchablePreferenceScreenDAO.persist(graph.vertexSet());
    }

    public Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> load() {
        return convertScreensToGraph(searchablePreferenceScreenDAO.loadAll());
    }

    private void removePersistedGraph() {
        searchablePreferenceScreenDAO.removeAll();
    }

    private void detachGraphFromDb(final Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> graph) {
        searchablePreferenceScreenDAO.detachScreensFromDb(graph.vertexSet());
    }
}
