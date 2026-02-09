package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.collect.MoreCollectors;
import com.google.common.graph.ValueGraph;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public final class Tree<N, V, G extends ValueGraph<N, V>> {

    private final G graph;
    private final TreePathProvider<N, V, G> treePathProvider = new TreePathProvider<>(this);

    public Tree(final G graph) {
        TreeValidator.validateIsTree(graph).throwIfInvalid();
        this.graph = graph;
    }

    public G graph() {
        return graph;
    }

    // FK-TODO: add cache for rootNode?
    public N rootNode() {
        return Graphs.getRootNode(graph).orElseThrow();
    }

    public Optional<N> parentNodeOf(final N node) {
        return this
                .incomingEdgeOf(node)
                .map(incomingEdgeOfNode -> incomingEdgeOfNode.endpointPair().source());
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
        return treePathProvider.getPathFromRootNodeToTarget(target);
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final Tree<?, ?, ?> tree = (Tree<?, ?, ?>) o;
        return Objects.equals(graph, tree.graph);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(graph);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Tree.class.getSimpleName() + "[", "]")
                .add("graph=" + graph)
                .toString();
    }
}
