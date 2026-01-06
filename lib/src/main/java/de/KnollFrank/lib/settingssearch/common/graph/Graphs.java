package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.collect.MoreCollectors;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.cycle.CycleDetector;
import org.jgrapht.alg.shortestpath.BFSShortestPath;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.common.Sets;

public class Graphs {

    public static <V> Optional<V> getRootNode(final Graph<V, ?> graph) {
        return graph
                .vertexSet()
                .stream()
                .filter(node -> isRootNode(graph, node))
                .collect(MoreCollectors.toOptional());
    }

    public static <V, E> GraphPath<V, E> getPathFromRootNodeToTarget(final Graph<V, E> graph, final V target) {
        final V root = getRootNode(graph).orElseThrow();
        return Optional
                .ofNullable(BFSShortestPath.findPathBetween(graph, root, target))
                .orElseThrow(() -> new IllegalStateException("No path found in graph from root '" + root + "' to target '" + target + "'"));
    }

    public static <V, E> Optional<E> getIncomingEdgeOfNode(final Graph<V, E> graph, final V node) {
        return Sets.asOptional(graph.incomingEdgesOf(node));
    }

    public static <V, E> void assertIsTree(final Graph<V, E> graph) {
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
        if (getRootNode(graph).isEmpty()) {
            throw new IllegalArgumentException("A Tree must have exactly one root node (in-degree 0).");
        }
    }

    private static <V> boolean isRootNode(final Graph<V, ?> graph, final V node) {
        return graph.inDegreeOf(node) == 0;
    }

    private static <V, E> boolean hasMaxOneParent(final Graph<V, E> graph) {
        return graph
                .vertexSet()
                .stream()
                .allMatch(v -> graph.inDegreeOf(v) <= 1);
    }
}
