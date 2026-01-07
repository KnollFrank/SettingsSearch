package de.KnollFrank.lib.settingssearch.common.graph;

import org.jgrapht.Graph;

public record Subtree<V, E>(UnmodifiableTree<V, E> tree, V rootNodeOfSubtree) {

    public Subtree {
        if (!tree.graph().containsVertex(rootNodeOfSubtree)) {
            throw new IllegalArgumentException("graph '" + tree + "' must contain rootNodeOfSubtree '" + rootNodeOfSubtree + "'");
        }
    }

    public static <V, E> Subtree<V, E> of(final Graph<V, E> graph) {
        return new Subtree<>(
                UnmodifiableTree.of(graph),
                Graphs.getRootNode(graph).orElseThrow());
    }

    public GraphAtNode<V, E> asGraphAtNode() {
        return new GraphAtNode<>(tree.graph(), rootNodeOfSubtree);
    }
}
