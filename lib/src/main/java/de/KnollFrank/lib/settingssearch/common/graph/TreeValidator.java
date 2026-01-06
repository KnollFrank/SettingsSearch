package de.KnollFrank.lib.settingssearch.common.graph;

import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.cycle.CycleDetector;

class TreeValidator {

    public static <V, E> TreeValidatorResult validateIsTree(final Graph<V, E> graph) {
        if (!graph.getType().isDirected()) {
            return TreeValidatorResult.invalid("A Tree must be a directed graph.");
        }
        if (graph.vertexSet().isEmpty()) {
            return TreeValidatorResult.invalid("The graph must not be empty.");
        }
        if (!new ConnectivityInspector<>(graph).isConnected()) {
            return TreeValidatorResult.invalid("The graph must be connected.");
        }
        if (new CycleDetector<>(graph).detectCycles()) {
            return TreeValidatorResult.invalid("The graph contains cycles; a tree must be acyclic.");
        }
        if (!hasMaxOneParent(graph)) {
            return TreeValidatorResult.invalid("Each node in a directed tree must have an in-degree of at most 1.");
        }
        {
            final int rootCount = Graphs.getRootNodes(graph).size();
            if (rootCount != 1) {
                return TreeValidatorResult.invalid("A Tree must have exactly one root node, but found: " + rootCount);
            }
        }
        return TreeValidatorResult.valid();
    }

    private static <V, E> boolean hasMaxOneParent(final Graph<V, E> graph) {
        return graph
                .vertexSet()
                .stream()
                .allMatch(v -> graph.inDegreeOf(v) <= 1);
    }
}
