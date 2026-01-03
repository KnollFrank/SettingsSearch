package de.KnollFrank.lib.settingssearch.common.graph;

import org.jgrapht.Graph;

// FK-TODO: enforce tree contains rootNodeOfSubtree
public record Subtree<V, E>(Graph<V, E> tree, V rootNodeOfSubtree) {

    public static <V, E> Subtree<V, E> of(final Graph<V, E> graph) {
        return new Subtree<>(
                graph,
                Graphs.getRootNode(graph).orElseThrow());
    }

    public GraphAtNode<V, E> asGraphAtNode() {
        return new GraphAtNode<>(tree, rootNodeOfSubtree);
    }
}
