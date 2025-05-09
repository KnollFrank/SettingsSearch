package de.KnollFrank.lib.settingssearch.db.preference.dao;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.builder.GraphBuilder;

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
        addNodes(graphBuilder, screens);
        addEdges(graphBuilder, getEdgeDescriptions(screens));
        return graphBuilder.build();
    }

    private record EdgeDescription(SearchablePreferenceScreen source,
                                   SearchablePreferenceScreen target,
                                   SearchablePreferenceEdge edge) {
    }

    // FK-TODO: refactor
    private List<EdgeDescription> getEdgeDescriptions(final List<SearchablePreferenceScreen> screens) {
        final Builder<EdgeDescription> edgeDescriptionsBuilder = ImmutableList.builder();
        for (final SearchablePreferenceScreen targetScreen : screens) {
            for (final SearchablePreference sourcePreference : getSourcePreferences(targetScreen)) {
                final SearchablePreferenceScreen sourceScreen = getSearchablePreferenceScreen(sourcePreference);
                edgeDescriptionsBuilder.add(new EdgeDescription(sourceScreen, targetScreen, new SearchablePreferenceEdge(sourcePreference)));
            }
        }
        return edgeDescriptionsBuilder.build();
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

    private static void addNodes(final GraphBuilder<SearchablePreferenceScreen, SearchablePreferenceEdge, ? extends DefaultDirectedGraph<SearchablePreferenceScreen, SearchablePreferenceEdge>> graphBuilder,
                                 final List<SearchablePreferenceScreen> nodes) {
        nodes.forEach(graphBuilder::addVertex);
    }

    private void addEdges(final GraphBuilder<SearchablePreferenceScreen, SearchablePreferenceEdge, ? extends DefaultDirectedGraph<SearchablePreferenceScreen, SearchablePreferenceEdge>> graphBuilder,
                          final List<EdgeDescription> edgeDescriptions) {
        edgeDescriptions.forEach(
                edgeDescription ->
                        graphBuilder.addEdge(
                                edgeDescription.source(),
                                edgeDescription.target(),
                                edgeDescription.edge()));
    }
}
