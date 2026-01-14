package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.ValueGraph;

import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Sets;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
class EdgesProvider {

    public static <N, V> Set<Edge<N, V>> getIncomingEdgesOfNode(final ValueGraph<N, V> graph,
                                                                final N node) {
        return getFilteredEdgesOfNode(graph, node, isIncomingEdgeOf(node));
    }

    public static <N, V> Set<Edge<N, V>> getOutgoingEdgesOfNode(final ValueGraph<N, V> graph,
                                                                final N node) {
        return getFilteredEdgesOfNode(graph, node, isOutgoingEdgeOf(node));
    }

    private static <N> Predicate<EndpointPair<N>> isIncomingEdgeOf(final N node) {
        return edge -> edge.target().equals(node);
    }

    private static <N> Predicate<EndpointPair<N>> isOutgoingEdgeOf(final N node) {
        return edge -> edge.source().equals(node);
    }

    private static <N, V> Set<Edge<N, V>> getFilteredEdgesOfNode(
            final ValueGraph<N, V> graph,
            final N node,
            final Predicate<EndpointPair<N>> predicate) {
        return Sets
                .filterElementsByPredicate(
                        graph.incidentEdges(node),
                        predicate)
                .stream()
                .map(endpointPair -> new Edge<>(endpointPair, graph.edgeValueOrDefault(endpointPair, null)))
                .collect(Collectors.toSet());
    }
}
