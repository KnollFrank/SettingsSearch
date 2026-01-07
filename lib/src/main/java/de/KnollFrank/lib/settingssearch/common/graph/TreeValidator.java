package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.collect.Iterators;

import org.jgrapht.Graph;
import org.jgrapht.traverse.BreadthFirstIterator;

import java.util.Set;

class TreeValidator {

    public static <V, E> TreeValidatorResult validateIsTree(final Graph<V, E> graph) {
        if (!graph.getType().isDirected()) {
            return TreeValidatorResult.invalid("A Tree must be a directed graph.");
        }
        if (graph.vertexSet().isEmpty()) {
            return TreeValidatorResult.invalid("The graph must not be empty.");
        }
        {
            final int numberOfNodes = graph.vertexSet().size();
            final int numberOfEdges = graph.edgeSet().size();
            if (numberOfEdges != numberOfNodes - 1) {
                return TreeValidatorResult.invalid(
                        String.format(
                                "A tree with %d nodes must have exactly %d edges, but found %d.",
                                numberOfNodes,
                                numberOfNodes - 1,
                                numberOfEdges));
            }
        }
        if (!hasMaxOneParent(graph)) {
            return TreeValidatorResult.invalid("Each node in a directed tree must have an in-degree of at most 1.");
        }
        {
            final Set<V> rootNodes = Graphs.getRootNodes(graph);
            if (rootNodes.size() != 1) {
                return TreeValidatorResult.invalid("A Tree must have exactly one root node, but found: " + rootNodes.size());
            }
        }
        if (!allNodesReachableFromRootNode(graph)) {
            return TreeValidatorResult.invalid("The graph is not connected; not all nodes are reachable from the root.");
        }
        return TreeValidatorResult.valid();
    }

    private static <V, E> boolean hasMaxOneParent(final Graph<V, E> graph) {
        return graph
                .vertexSet()
                .stream()
                .allMatch(v -> graph.inDegreeOf(v) <= 1);
    }

    private static <V, E> boolean allNodesReachableFromRootNode(final Graph<V, E> graph) {
        return getReachableCountFromRootNode(graph) == graph.vertexSet().size();
    }

    private static <V, E> int getReachableCountFromRootNode(final Graph<V, E> graph) {
        return Iterators.size(
                new BreadthFirstIterator<>(
                        graph,
                        Graphs.getRootNode(graph).orElseThrow()));
    }
}
