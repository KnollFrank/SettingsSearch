package de.KnollFrank.lib.settingssearch.db.preference.dao;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
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

    public Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> load() {
        final var graphBuilder = DefaultDirectedGraph.<SearchablePreferenceScreen, SearchablePreferenceEdge>createBuilder(SearchablePreferenceEdge.class);
        final List<SearchablePreferenceScreen> screens = searchablePreferenceScreenDAO.loadAll();
        screens.forEach(graphBuilder::addVertex);
        for (final SearchablePreferenceScreen targetScreen : screens) {
            for (final SearchablePreference sourcePreference : getSourcePreferences(targetScreen)) {
                final SearchablePreferenceScreen sourceScreen = getSearchablePreferenceScreen(sourcePreference);
                graphBuilder.addEdge(
                        sourceScreen,
                        targetScreen,
                        new SearchablePreferenceEdge(sourcePreference));
            }
        }
        return graphBuilder.build();
    }

    private static Set<SearchablePreference> getSourcePreferences(final SearchablePreferenceScreen targetScreen) {
        return targetScreen
                .getAllPreferences()
                .stream()
                .map(SearchablePreference::getPredecessor)
                .filter(Optional::isPresent)
                .map(Optional::orElseThrow)
                .collect(Collectors.toSet());
    }

    private SearchablePreferenceScreen getSearchablePreferenceScreen(final SearchablePreference preference) {
        return searchablePreferenceScreenDAO
                .findSearchablePreferenceScreenById(preference.getSearchablePreferenceScreenId())
                .orElseThrow();
    }
}
