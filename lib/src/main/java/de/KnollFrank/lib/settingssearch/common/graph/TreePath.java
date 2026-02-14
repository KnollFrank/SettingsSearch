package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.collect.ImmutableList;
import com.google.common.graph.ValueGraph;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

import de.KnollFrank.lib.settingssearch.common.Lists;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public final class TreePath<N, V, G extends ValueGraph<N, V>> {

    private final Tree<N, V, G> tree;
    private final List<N> nodes;
    private Optional<List<Edge<N, V>>> edgesOnPath = Optional.empty();

    public TreePath(final Tree<N, V, G> tree, final List<N> nodes) {
        if (nodes.isEmpty()) {
            throw new IllegalArgumentException("Path nodes cannot be empty.");
        }
        if (!tree.graph().nodes().containsAll(nodes)) {
            throw new IllegalArgumentException("All nodes of the TreePath must be part of the tree.");
        }
        // FK-TODO: add assertion that nodes form a real path of the tree?
        this.tree = tree;
        this.nodes = nodes;
    }

    public TreePath<N, V, G> add(final N node) {
        return new TreePath<>(
                tree,
                ImmutableList
                        .<N>builder()
                        .addAll(nodes)
                        .add(node)
                        .build());
    }

    public N startNode() {
        return Lists.getHead(nodes).orElseThrow();
    }

    public N endNode() {
        return Lists.getLastElement(nodes).orElseThrow();
    }

    public List<Edge<N, V>> edges() {
        if (edgesOnPath.isEmpty()) {
            edgesOnPath = Optional.of(Trees.getEdgesOnPath(this));
        }
        return edgesOnPath.orElseThrow();
    }

    public Tree<N, V, G> tree() {
        return tree;
    }

    public List<N> nodes() {
        return nodes;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final TreePath<?, ?, ?> treePath = (TreePath<?, ?, ?>) o;
        return Objects.equals(tree, treePath.tree) && Objects.equals(nodes, treePath.nodes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tree, nodes);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TreePath.class.getSimpleName() + "[", "]")
                .add("tree=" + tree)
                .add("nodes=" + nodes)
                .toString();
    }
}