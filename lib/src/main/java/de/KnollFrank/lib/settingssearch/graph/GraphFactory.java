package de.KnollFrank.lib.settingssearch.graph;

import com.google.common.graph.MutableValueGraph;

class GraphFactory {

    @SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
    public static <N, V> MutableValueGraph<N, V> createEmptyGraph(final GraphListener<N> graphListener) {
        return new ListenableMutableValueGraph<>(graphListener);
    }
}
