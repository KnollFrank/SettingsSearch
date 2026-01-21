package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.collect.MoreCollectors;
import com.google.common.graph.ValueGraph;

import java.util.Optional;
import java.util.Set;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public record Tree<N, V, G extends ValueGraph<N, V>>(G graph) {

    public Tree {
        TreeValidator.validateIsTree(graph).throwIfInvalid();
    }

    public N rootNode() {
        return Graphs.getRootNode(graph).orElseThrow();
    }

    public Optional<N> parentNodeOf(final N node) {
        // FK-TODO: implement using incomingEdgeOf()
        return graph
                .predecessors(node)
                .stream()
                .collect(MoreCollectors.toOptional());
    }

    public Optional<Edge<N, V>> incomingEdgeOf(final N node) {
        return Graphs
                .getIncomingEdgesOfNode(graph, node)
                .stream()
                .collect(MoreCollectors.toOptional());
    }

    public Set<Edge<N, V>> outgoingEdgesOf(final N node) {
        return Graphs.getOutgoingEdgesOfNode(graph, node);
    }

    public TreePath<N, V, G> getPathFromRootNodeToTarget(final N target) {
        return Trees.getPathFromRootNodeToTarget(this, target);
    }
}
