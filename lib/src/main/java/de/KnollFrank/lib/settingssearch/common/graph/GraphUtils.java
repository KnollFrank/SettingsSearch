package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.collect.MoreCollectors;

import org.jgrapht.Graph;

public class GraphUtils {

    public static <V> V getRootNode(final Graph<V, ?> graph) {
        return graph
                .vertexSet()
                .stream()
                .filter(vertex -> graph.inDegreeOf(vertex) == 0)
                .collect(MoreCollectors.onlyElement());
    }
}
