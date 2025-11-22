package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.collect.MoreCollectors;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.BFSShortestPath;

import java.util.Optional;

public class GraphUtils {

    public static <V> Optional<V> getRootNode(final Graph<V, ?> graph) {
        return graph
                .vertexSet()
                .stream()
                .filter(vertex -> graph.inDegreeOf(vertex) == 0)
                .collect(MoreCollectors.toOptional());
    }

    public static <V, E> GraphPath<V, E> getPathFromRootNodeToSink(final Graph<V, E> graph, final V sink) {
        final V root = getRootNode(graph).orElseThrow();
        return Optional
                .ofNullable(BFSShortestPath.findPathBetween(graph, root, sink))
                .orElseThrow(() -> new IllegalStateException("No path found in graph from root '" + root + "' to sink '" + sink + "'"));
    }
}
