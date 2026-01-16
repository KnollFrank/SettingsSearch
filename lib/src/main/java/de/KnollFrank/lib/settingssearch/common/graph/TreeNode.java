package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.graph.ValueGraph;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public record TreeNode<N, V, G extends ValueGraph<N, V>>(N node, Tree<N, V, G> tree) {

    public TreeNode {
        if (!tree.graph().nodes().contains(node)) {
            throw new IllegalArgumentException("tree '" + tree + "' must contain node '" + node + "'");
        }
    }
}
