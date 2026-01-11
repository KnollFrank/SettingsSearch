package de.KnollFrank.lib.settingssearch.common.graph;

@SuppressWarnings({"UnstableApiUsage"})
public record TreeAtNode<V, E>(Tree<V, E> tree, V nodeOfTree) {

    public TreeAtNode {
        if (!tree.graph().nodes().contains(nodeOfTree)) {
            throw new IllegalArgumentException("tree '" + tree + "' must contain nodeOfTree '" + nodeOfTree + "'");
        }
    }
}
