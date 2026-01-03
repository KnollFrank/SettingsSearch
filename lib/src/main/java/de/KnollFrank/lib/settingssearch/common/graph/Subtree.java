package de.KnollFrank.lib.settingssearch.common.graph;

import org.jgrapht.Graph;

public record Subtree<V, E>(Graph<V, E> graph, V rootNodeOfSubtree) {

    public Subtree {
        if (!graph.containsVertex(rootNodeOfSubtree)) {
            throw new IllegalArgumentException("graph '" + graph + "' must contain rootNodeOfSubtree '" + rootNodeOfSubtree + "'");
        }
    }

    public static <V, E> Subtree<V, E> of(final Graph<V, E> graph) {
        return new Subtree<>(
                graph,
                Graphs.getRootNode(graph).orElseThrow());
    }

    public GraphAtNode<V, E> asGraphAtNode() {
        return new GraphAtNode<>(graph, rootNodeOfSubtree);
    }
}
