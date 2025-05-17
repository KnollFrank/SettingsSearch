package de.KnollFrank.lib.settingssearch.db.preference.dao;

import com.google.common.collect.ImmutableSet;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.builder.GraphBuilder;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

// FK-TODO: make this class a @Dao and introduce a new @Entity named SearchablePreferenceScreenGraph which references it's root SearchablePreferenceScreen. Then remove SearchDatabaseStateDAO and SearchDatabaseState.
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
        final Set<SearchablePreferenceScreen> screens = searchablePreferenceScreenDAO.loadAll();
        addNodes(graphBuilder, screens);
        addEdges(graphBuilder, getEdgeDescriptions(screens));
        return graphBuilder.build();
    }

    private record EdgeDescription(SearchablePreferenceScreen source,
                                   SearchablePreferenceScreen target,
                                   SearchablePreferenceEdge edge) {
    }

    private Set<EdgeDescription> getEdgeDescriptions(final Set<SearchablePreferenceScreen> screens) {
        final ImmutableSet.Builder<EdgeDescription> edgeDescriptionsBuilder = ImmutableSet.builder();
        for (final SearchablePreferenceScreen targetScreen : screens) {
            for (final SearchablePreference sourcePreference : getSourcePreferences(targetScreen)) {
                edgeDescriptionsBuilder.add(
                        new EdgeDescription(
                                getSearchablePreferenceScreen(sourcePreference),
                                targetScreen,
                                new SearchablePreferenceEdge(sourcePreference)));
            }
        }
        return edgeDescriptionsBuilder.build();
    }

    private static Set<SearchablePreference> getSourcePreferences(final SearchablePreferenceScreen targetScreen) {
        return targetScreen
                .getAllPreferences()
                .stream()
                .map(SearchablePreference::getPredecessor)
                // Fk-TODO: use mapMulti() if API level is at least 34
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
                                 final Set<SearchablePreferenceScreen> nodes) {
        nodes.forEach(graphBuilder::addVertex);
    }

    private void addEdges(final GraphBuilder<SearchablePreferenceScreen, SearchablePreferenceEdge, ? extends DefaultDirectedGraph<SearchablePreferenceScreen, SearchablePreferenceEdge>> graphBuilder,
                          final Set<EdgeDescription> edgeDescriptions) {
        edgeDescriptions.forEach(
                edgeDescription ->
                        graphBuilder.addEdge(
                                edgeDescription.source(),
                                edgeDescription.target(),
                                edgeDescription.edge()));
    }
}
