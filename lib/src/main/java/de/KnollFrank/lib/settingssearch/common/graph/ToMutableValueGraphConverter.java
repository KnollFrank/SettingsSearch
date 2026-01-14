package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
class ToMutableValueGraphConverter {

    public static <N, V> MutableValueGraph<N, V> toMutableValueGraph(final ImmutableValueGraph<N, V> graph) {
        final MutableValueGraph<N, V> mutableCopy = ValueGraphBuilder.from(graph).build();
        copyNodes(graph, mutableCopy);
        copyEdges(graph, mutableCopy);
        return mutableCopy;
    }

    private static <N, V> void copyNodes(final ImmutableValueGraph<N, V> src, final MutableValueGraph<N, V> dst) {
        src.nodes().forEach(dst::addNode);
    }

    private static <N, V> void copyEdges(final ImmutableValueGraph<N, V> src, final MutableValueGraph<N, V> dst) {
        for (final EndpointPair<N> edge : src.edges()) {
            dst.putEdgeValue(
                    edge.source(),
                    edge.target(),
                    src.edgeValueOrDefault(edge, null));
        }
    }
}
