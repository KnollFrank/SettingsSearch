package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.ValueGraph;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public record Edge<N, V>(EndpointPair<N> endpointPair, V value) {

    public static <N, V> Edge<N, V> of(final EndpointPair<N> endpointPair, final ValueGraph<N, V> graph) {
        return new Edge<>(endpointPair, Graphs.getEdgeValue(endpointPair, graph));
    }
}
