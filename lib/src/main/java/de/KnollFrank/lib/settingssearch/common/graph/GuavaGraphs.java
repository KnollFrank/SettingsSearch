package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.collect.MoreCollectors;
import com.google.common.graph.ValueGraph;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class GuavaGraphs {

    public static <Node, Value> boolean isEmpty(final ValueGraph<Node, Value> graph) {
        return graph.nodes().isEmpty();
    }

    public static <Node> Optional<Node> getRootNode(final ValueGraph<Node, ?> graph) {
        return GuavaGraphs
                .getRootNodes(graph)
                .stream()
                .collect(MoreCollectors.toOptional());
    }

    public static <Node> Set<Node> getRootNodes(final ValueGraph<Node, ?> graph) {
        return graph
                .nodes()
                .stream()
                .filter(node -> isRootNode(graph, node))
                .collect(Collectors.toSet());
    }

    private static <Node> boolean isRootNode(final ValueGraph<Node, ?> graph, final Node node) {
        return graph.inDegree(node) == 0;
    }
}
