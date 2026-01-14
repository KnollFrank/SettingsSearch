package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
class ToMutableValueGraphConverter {

    public static <N, V> MutableValueGraph<N, V> toMutableValueGraph(final ImmutableValueGraph<N, V> graph) {
        final MutableValueGraph<N, V> mutableCopy = ValueGraphBuilder.from(graph).build();
        GraphCopiers.copyNodesAndEdges(graph, mutableCopy);
        return mutableCopy;
    }
}
