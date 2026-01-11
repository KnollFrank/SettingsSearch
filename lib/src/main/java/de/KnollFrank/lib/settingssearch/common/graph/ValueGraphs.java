package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.collect.MoreCollectors;
import com.google.common.graph.EndpointPair;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraph;
import com.google.common.graph.ValueGraphBuilder;

import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Sets;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class ValueGraphs {

    public static <Node, Value> boolean isEmpty(final ValueGraph<Node, Value> graph) {
        return graph.nodes().isEmpty();
    }

    public static <Node> Optional<Node> getRootNode(final ValueGraph<Node, ?> graph) {
        return ValueGraphs
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

    public static <N> Set<EndpointPair<N>> getIncomingEdgesOfNode(final ValueGraph<N, ?> graph,
                                                                  final N node) {
        final Predicate<EndpointPair<N>> isIncomingEdgeOfNode = edge -> edge.target().equals(node);
        return Sets.filterElementsByPredicate(
                graph.incidentEdges(node),
                isIncomingEdgeOfNode);
    }

    public static <N> Set<EndpointPair<N>> getOutgoingEdgesOfNode(final ValueGraph<N, ?> graph,
                                                                  final N node) {
        final Predicate<EndpointPair<N>> isOutgoingEdgeOfNode = edge -> edge.source().equals(node);
        return Sets.filterElementsByPredicate(
                graph.incidentEdges(node),
                isOutgoingEdgeOfNode);
    }

    public static <N, V> MutableValueGraph<N, V> toMutableValueGraph(final ValueGraph<N, V> graph) {
        final MutableValueGraph<N, V> mutableCopy = ValueGraphBuilder.from(graph).build();
        graph.nodes().forEach(mutableCopy::addNode);
        for (final EndpointPair<N> edge : graph.edges()) {
            mutableCopy.putEdgeValue(
                    edge.source(),
                    edge.target(),
                    graph.edgeValueOrDefault(edge, null));
        }
        return mutableCopy;
    }

    private static <Node> boolean isRootNode(final ValueGraph<Node, ?> graph, final Node node) {
        return graph.inDegree(node) == 0;
    }
}
