package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.collect.MoreCollectors;
import com.google.common.graph.EndpointPair;
import com.google.common.graph.ValueGraph;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class TypedTree<N, V, G extends ValueGraph<N, V>> {

    private final G graph;

    public TypedTree(final G graph) {
        TreeValidator.validateIsTree(graph).throwIfInvalid();
        this.graph = graph;
    }

    public G graph() {
        return this.graph;
    }

    public N rootNode() {
        return ValueGraphs.getRootNode(graph).orElseThrow();
    }

    public Optional<N> parentNodeOf(final N node) {
        return graph
                .predecessors(node)
                .stream()
                .collect(MoreCollectors.toOptional());
    }

    public Optional<EndpointPair<N>> incomingEdgeOf(final N node) {
        return ValueGraphs
                .getIncomingEdgesOfNode(graph, node)
                .stream()
                .collect(MoreCollectors.toOptional());
    }

    public Set<EndpointPair<N>> outgoingEdgesOf(final N node) {
        return ValueGraphs.getOutgoingEdgesOfNode(graph, node);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final TypedTree<?, ?, ?> tree = (TypedTree<?, ?, ?>) o;
        return Objects.equals(graph, tree.graph);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(graph);
    }

    @Override
    public String toString() {
        return "TypedTree{" +
                "graph=" + graph +
                '}';
    }
}
