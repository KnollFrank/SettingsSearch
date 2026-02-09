package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.collect.ImmutableList;
import com.google.common.graph.ValueGraph;

import java.util.List;

import de.KnollFrank.lib.settingssearch.common.Lists;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public record TreePath<N, V, G extends ValueGraph<N, V>>(Tree<N, V, G> tree, List<N> nodes) {

    public TreePath {
        if (nodes.isEmpty()) {
            throw new IllegalArgumentException("Path nodes cannot be empty.");
        }
        if (!tree.graph().nodes().containsAll(nodes)) {
            throw new IllegalArgumentException("All nodes of the TreePath must be part of the tree.");
        }
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
        return Trees.getEdgesOnPath(this);
    }
}