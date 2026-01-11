package de.KnollFrank.lib.settingssearch.common.graph;

import java.util.List;

public record TreePath<N, V>(Tree<N, V> tree, List<N> nodes) {

    public TreePath {
        if (nodes.isEmpty()) {
            throw new IllegalArgumentException("Path nodes cannot be empty.");
        }
    }
}