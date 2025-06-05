package de.KnollFrank.lib.settingssearch.graph;

import com.google.common.collect.ImmutableSet;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.builder.GraphBuilder;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProviders;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntityEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;

public class SearchablePreferenceScreens2GraphConverter {

    public static Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> convertScreensToGraph(final Set<SearchablePreferenceScreenEntity> screens,
                                                                                                                final DbDataProviders dbDataProviders) {
        final var graphBuilder = DefaultDirectedGraph.<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge>createBuilder(SearchablePreferenceEntityEdge.class);
        addNodes(graphBuilder, screens);
        addEdges(graphBuilder, getEdgeDescriptions(screens, dbDataProviders));
        return graphBuilder.build();
    }

    private record EdgeDescription(SearchablePreferenceScreenEntity source,
                                   SearchablePreferenceScreenEntity target,
                                   SearchablePreferenceEntityEdge edge) {
    }

    private static Set<EdgeDescription> getEdgeDescriptions(final Set<SearchablePreferenceScreenEntity> screens,
                                                            final DbDataProviders dbDataProviders) {
        final ImmutableSet.Builder<EdgeDescription> edgeDescriptionsBuilder = ImmutableSet.builder();
        for (final SearchablePreferenceScreenEntity targetScreen : screens) {
            for (final SearchablePreferenceEntity sourcePreference : getSourcePreferences(targetScreen, dbDataProviders)) {
                edgeDescriptionsBuilder.add(
                        new EdgeDescription(
                                sourcePreference.getHost(dbDataProviders.preferencedbDataProvider()),
                                targetScreen,
                                new SearchablePreferenceEntityEdge(sourcePreference)));
            }
        }
        return edgeDescriptionsBuilder.build();
    }

    private static Set<SearchablePreferenceEntity> getSourcePreferences(final SearchablePreferenceScreenEntity targetScreen,
                                                                        final DbDataProviders dbDataProviders) {
        return targetScreen
                .getAllPreferences(dbDataProviders.screenDbDataProvider())
                .stream()
                .map(preference -> preference.getPredecessor(dbDataProviders.preferencedbDataProvider()))
                // Fk-TODO: use mapMulti() if API level is at least 34
                .filter(Optional::isPresent)
                .map(Optional::orElseThrow)
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
