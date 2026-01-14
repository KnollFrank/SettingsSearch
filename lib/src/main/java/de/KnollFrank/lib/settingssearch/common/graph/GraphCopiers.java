package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.MutableValueGraph;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
class GraphCopiers {

    public static <N, V> void copySrcToDst(final ImmutableValueGraph<N, V> src, final MutableValueGraph<N, V> dst) {
        copyNodes(src, dst);
        copyEdges(src, dst);
    }

    private static <N, V> void copyNodes(final ImmutableValueGraph<N, V> src, final MutableValueGraph<N, V> dst) {
        src.nodes().forEach(dst::addNode);
    }

    private static <N, V> void copyEdges(final ImmutableValueGraph<N, V> src, final MutableValueGraph<N, V> dst) {
        for (final EndpointPair<N> edge : src.edges()) {
            dst.putEdgeValue(
                    edge,
                    src.edgeValueOrDefault(edge, null));
        }
    }
}
