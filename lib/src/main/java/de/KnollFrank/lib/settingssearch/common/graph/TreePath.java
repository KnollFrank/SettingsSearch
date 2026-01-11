package de.KnollFrank.lib.settingssearch.common.graph;

import java.util.List;

import de.KnollFrank.lib.settingssearch.common.Lists;

public record TreePath<N, V>(Tree<N, V> tree, List<N> nodes) {

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
}