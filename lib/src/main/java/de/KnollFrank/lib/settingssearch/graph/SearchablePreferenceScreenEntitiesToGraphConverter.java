package de.KnollFrank.lib.settingssearch.graph;

import com.google.common.collect.ImmutableSet;
import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.ValueGraph;
import com.google.common.graph.ValueGraphBuilder;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class SearchablePreferenceScreenEntitiesToGraphConverter {

    public static Tree<SearchablePreferenceScreenEntity, SearchablePreferenceEntity> convertScreensToTree(
            final Set<SearchablePreferenceScreenEntity> screens,
            final DbDataProvider dbDataProvider) {
        return new Tree<>(convertScreensToGraph(screens, dbDataProvider));
    }

    private static ValueGraph<SearchablePreferenceScreenEntity, SearchablePreferenceEntity> convertScreensToGraph(
            final Set<SearchablePreferenceScreenEntity> screens,
            final DbDataProvider dbDataProvider) {
        final ImmutableValueGraph.Builder<SearchablePreferenceScreenEntity, SearchablePreferenceEntity> graphBuilder = ValueGraphBuilder.directed().immutable();
        addNodes(graphBuilder, screens);
        addEdges(graphBuilder, getEdgeDescriptions(screens, dbDataProvider));
        return graphBuilder.build();
    }

    private record EdgeDescription(SearchablePreferenceScreenEntity source,
                                   SearchablePreferenceScreenEntity target,
                                   SearchablePreferenceEntity edgeValue) {
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
                                sourcePreference));
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

    private static void addNodes(final ImmutableValueGraph.Builder<SearchablePreferenceScreenEntity, SearchablePreferenceEntity> graphBuilder,
                                 final Set<SearchablePreferenceScreenEntity> nodes) {
        nodes.forEach(graphBuilder::addNode);
    }

    private static void addEdges(final ImmutableValueGraph.Builder<SearchablePreferenceScreenEntity, SearchablePreferenceEntity> graphBuilder,
                                 final Set<EdgeDescription> edgeDescriptions) {
        edgeDescriptions.forEach(
                edgeDescription ->
                        graphBuilder.putEdgeValue(
                                edgeDescription.source(),
                                edgeDescription.target(),
                                edgeDescription.edgeValue()));
    }
}
