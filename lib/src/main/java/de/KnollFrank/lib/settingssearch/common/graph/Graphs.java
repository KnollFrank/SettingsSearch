package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.collect.MoreCollectors;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.BFSShortestPath;

import java.util.Optional;

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

    private static <V> boolean isRootNode(final Graph<V, ?> graph, final V node) {
        return graph.inDegreeOf(node) == 0;
    }
}
