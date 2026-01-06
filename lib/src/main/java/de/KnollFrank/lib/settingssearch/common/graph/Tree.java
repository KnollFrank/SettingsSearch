package de.KnollFrank.lib.settingssearch.common.graph;

import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.cycle.CycleDetector;

public record Tree<V, E>(Graph<V, E> graph) {

    public Tree {
        if (!graph.getType().isDirected()) {
            throw new IllegalArgumentException("A Tree must be a directed graph.");
        }
        if (graph.vertexSet().isEmpty() || !new ConnectivityInspector<>(graph).isConnected()) {
            throw new IllegalArgumentException("The graph must be connected and not empty.");
        }
        if (new CycleDetector<>(graph).detectCycles()) {
            throw new IllegalArgumentException("The graph contains cycles; a tree must be acyclic.");
        }
        if (!hasMaxOneParent(graph)) {
            throw new IllegalArgumentException("Each node in a directed tree must have an in-degree of at most 1.");
        }
        if (Graphs.getRootNode(graph).isEmpty()) {
            throw new IllegalArgumentException("A Tree must have exactly one root node (in-degree 0).");
        }
    }

    public V getRoot() {
        return Graphs
                .getRootNode(graph)
                .orElseThrow();
    }

    private static <V, E> boolean hasMaxOneParent(final Graph<V, E> graph) {
        return graph
                .vertexSet()
                .stream()
                .allMatch(v -> graph.inDegreeOf(v) <= 1);
    }
}
