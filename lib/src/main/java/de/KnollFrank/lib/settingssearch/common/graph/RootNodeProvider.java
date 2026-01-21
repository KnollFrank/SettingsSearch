package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.collect.MoreCollectors;
import com.google.common.graph.ValueGraph;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
class RootNodeProvider {

    private RootNodeProvider() {
    }

    public static <N> Optional<N> getRootNode(final ValueGraph<N, ?> graph) {
        return RootNodeProvider
                .getRootNodes(graph)
                .stream()
                .collect(MoreCollectors.toOptional());
    }

    public static <N> Set<N> getRootNodes(final ValueGraph<N, ?> graph) {
        return graph
                .nodes()
                .stream()
                .filter(node -> isRootNode(graph, node))
                .collect(Collectors.toSet());
    }

    private static <N> boolean isRootNode(final ValueGraph<N, ?> graph, final N node) {
        return graph.inDegree(node) == 0;
    }
}
