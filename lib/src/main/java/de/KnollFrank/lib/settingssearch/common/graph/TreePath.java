package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.graph.ValueGraph;

import java.util.List;

import de.KnollFrank.lib.settingssearch.common.Lists;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public record TreePath<N, V, G extends ValueGraph<N, V>>(Tree<N, V, G> tree, List<N> nodes) {

    public TreePath {
        if (nodes.isEmpty()) {
            throw new IllegalArgumentException("Path nodes cannot be empty.");
        }
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