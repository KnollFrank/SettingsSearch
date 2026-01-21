package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraph;
import com.google.common.graph.ValueGraphBuilder;

import java.util.Optional;
import java.util.Set;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class Graphs {

    private Graphs() {
    }

    public static <Node, Value> boolean isEmpty(final ValueGraph<Node, Value> graph) {
        return graph.nodes().isEmpty();
    }

    public static <Node> Optional<Node> getRootNode(final ValueGraph<Node, ?> graph) {
        return RootNodeProvider.getRootNode(graph);
    }

    public static <Node> Set<Node> getRootNodes(final ValueGraph<Node, ?> graph) {
        return RootNodeProvider.getRootNodes(graph);
    }

    public static <N, V> Set<Edge<N, V>> getEdges(final ValueGraph<N, V> graph) {
        return EdgesProvider.getEdges(graph);
    }

    public static <N, V> void addEdge(final MutableValueGraph<N, V> graph, final Edge<N, V> edge) {
        graph.putEdgeValue(edge.endpointPair(), edge.value());
    }

    public static <N, V> Set<Edge<N, V>> getIncomingEdgesOfNode(final ValueGraph<N, V> graph,
                                                                final N node) {
        return EdgesProvider.getIncomingEdgesOfNode(graph, node);
    }

    public static <N, V> Set<Edge<N, V>> getOutgoingEdgesOfNode(final ValueGraph<N, V> graph,
                                                                final N node) {
        return EdgesProvider.getOutgoingEdgesOfNode(graph, node);
    }

    public static <N, V> MutableValueGraph<N, V> toMutableValueGraph(final ImmutableValueGraph<N, V> graph) {
        return ToMutableValueGraphConverter.toMutableValueGraph(graph);
    }

    public static <N, V> ImmutableValueGraph.Builder<N, V> directedImmutableValueGraphBuilder() {
        return ValueGraphBuilder.directed().immutable();
    }

    public static <N, V> MutableValueGraph<N, V> createEmptyDirectedMutableValueGraph() {
        return ValueGraphBuilder.directed().build();
    }

    public static <N, V> V getEdgeValue(final EndpointPair<N> endpointPair,
                                        final ValueGraph<N, V> graph) {
        return EdgesProvider.getEdgeValue(endpointPair, graph);
    }
}
