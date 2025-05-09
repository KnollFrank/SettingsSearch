package de.KnollFrank.lib.settingssearch.db.preference.dao;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;

import java.util.List;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

public class SearchablePreferenceScreenGraphDAO {

    private final SearchablePreferenceScreenDAO searchablePreferenceScreenDAO;

    public SearchablePreferenceScreenGraphDAO(final SearchablePreferenceScreenDAO searchablePreferenceScreenDAO) {
        this.searchablePreferenceScreenDAO = searchablePreferenceScreenDAO;
    }

    public void persist(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph) {
        searchablePreferenceScreenDAO.persist(graph.vertexSet());
    }

    // FK-TODO: refactor
    public Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> load() {
        final List<SearchablePreferenceScreen> searchablePreferenceScreens = searchablePreferenceScreenDAO.loadAll();
        final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph = new DefaultDirectedGraph<>(SearchablePreferenceEdge.class);
        searchablePreferenceScreens.forEach(graph::addVertex);
        searchablePreferenceScreens.forEach(targetScreen ->
                targetScreen
                        .getAllPreferences()
                        .forEach(targetPreference ->
                                targetPreference
                                        .getPredecessor()
                                        .ifPresent(sourcePreference ->
                                                searchablePreferenceScreenDAO
                                                        .findSearchablePreferenceScreenById(sourcePreference.getSearchablePreferenceScreenId())
                                                        .ifPresent(sourceScreen ->
                                                                graph.addEdge(
                                                                        sourceScreen,
                                                                        targetScreen,
                                                                        new SearchablePreferenceEdge(sourcePreference))))));
        return graph;
    }
}
