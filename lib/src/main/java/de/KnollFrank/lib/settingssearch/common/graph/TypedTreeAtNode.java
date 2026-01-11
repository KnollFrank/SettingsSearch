package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.graph.ValueGraph;

import java.util.Objects;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class TypedTreeAtNode<N, V, Graph extends ValueGraph<N, V>, TTree extends TypedTree<N, V, Graph>> {

    private final TTree tree;
    private final N nodeOfTree;

    public TypedTreeAtNode(final TTree tree, final N nodeOfTree) {
        if (!tree.graph().nodes().contains(nodeOfTree)) {
            throw new IllegalArgumentException("tree '" + tree + "' must contain nodeOfTree '" + nodeOfTree + "'");
        }
        this.tree = tree;
        this.nodeOfTree = nodeOfTree;
    }

    public TTree tree() {
        return tree;
    }

    public N nodeOfTree() {
        return nodeOfTree;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (TypedTreeAtNode<?, ?, ?, ?>) obj;
        return Objects.equals(this.tree, that.tree) &&
                Objects.equals(this.nodeOfTree, that.nodeOfTree);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tree, nodeOfTree);
    }

    @Override
    public String toString() {
        return "TypedTreeAtNode[" +
                "tree=" + tree + ", " +
                "nodeOfTree=" + nodeOfTree + ']';
    }
}
