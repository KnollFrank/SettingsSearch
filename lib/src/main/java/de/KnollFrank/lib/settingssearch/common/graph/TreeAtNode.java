package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.graph.ValueGraph;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
// FK-TODO: rename to TreeNode and switch order of fields tree and nodeOfTree
public record TreeAtNode<N, V, G extends ValueGraph<N, V>>(Tree<N, V, G> tree, N nodeOfTree) {

    public TreeAtNode {
        if (!tree.graph().nodes().contains(nodeOfTree)) {
            throw new IllegalArgumentException("tree '" + tree + "' must contain nodeOfTree '" + nodeOfTree + "'");
        }
    }
}
