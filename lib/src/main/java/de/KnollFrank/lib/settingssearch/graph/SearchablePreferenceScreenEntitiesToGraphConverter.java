package de.KnollFrank.lib.settingssearch.graph;

import com.google.common.collect.ImmutableSet;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.builder.GraphBuilder;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntityEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;

public class SearchablePreferenceScreenEntitiesToGraphConverter {

    public static Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> convertScreensToGraph(final Set<SearchablePreferenceScreenEntity> screens,
                                                                                                                final DbDataProvider dbDataProvider) {
        final var graphBuilder = DefaultDirectedGraph.<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge>createBuilder(SearchablePreferenceEntityEdge.class);
        addNodes(graphBuilder, screens);
        addEdges(graphBuilder, getEdgeDescriptions(screens, dbDataProvider));
        return graphBuilder.build();
    }

    private record EdgeDescription(SearchablePreferenceScreenEntity source,
                                   SearchablePreferenceScreenEntity target,
                                   SearchablePreferenceEntityEdge edge) {
    }

    private static Set<EdgeDescription> getEdgeDescriptions(final Set<SearchablePreferenceScreenEntity> screens,
                                                            final DbDataProvider dbDataProvider) {
        final ImmutableSet.Builder<EdgeDescription> edgeDescriptionsBuilder = ImmutableSet.builder();
        for (final SearchablePreferenceScreenEntity targetScreen : screens) {
            for (final SearchablePreferenceEntity sourcePreference : getSourcePreferences(targetScreen, dbDataProvider)) {
                edgeDescriptionsBuilder.add(
                        new EdgeDescription(
                                sourcePreference.getHost(dbDataProvider),
                                targetScreen,
                                new SearchablePreferenceEntityEdge(sourcePreference)));
            }
        }
        return edgeDescriptionsBuilder.build();
    }

    private static Set<SearchablePreferenceEntity> getSourcePreferences(final SearchablePreferenceScreenEntity targetScreen,
                                                                        final DbDataProvider dbDataProvider) {
        return targetScreen
                .getAllPreferencesOfPreferenceHierarchy(dbDataProvider)
                .stream()
                .map(preference -> preference.getPredecessor(dbDataProvider))
                .flatMap(Optional::stream)
                .collect(Collectors.toSet());
    }

    private static void addNodes(final GraphBuilder<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge, ? extends DefaultDirectedGraph<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge>> graphBuilder,
                                 final Set<SearchablePreferenceScreenEntity> nodes) {
        nodes.forEach(graphBuilder::addVertex);
    }

    private static void addEdges(final GraphBuilder<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge, ? extends DefaultDirectedGraph<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge>> graphBuilder,
                                 final Set<EdgeDescription> edgeDescriptions) {
        edgeDescriptions.forEach(
                edgeDescription ->
                        graphBuilder.addEdge(
                                edgeDescription.source(),
                                edgeDescription.target(),
                                edgeDescription.edge()));
    }
}
