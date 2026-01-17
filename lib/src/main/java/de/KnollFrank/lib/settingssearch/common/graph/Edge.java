package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.ValueGraph;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public record Edge<N, V>(EndpointPair<N> endpointPair, V value) {

    public static <N, V> Edge<N, V> of(final EndpointPair<N> endpointPair, final ValueGraph<N, V> graph) {
        return new Edge<>(endpointPair, Graphs.getEdgeValue(endpointPair, graph));
    }

    public boolean isSelfLoop() {
        return endpointPair.source().equals(endpointPair.target());
    }

    public Edge<N, V> asEdgeHavingSource(final N source) {
        return new Edge<>(
                EndpointPair.ordered(
                        source,
                        endpointPair.target()),
                value);
    }

    public Edge<N, V> asEdgeHavingTarget(final N target) {
        return new Edge<>(
                EndpointPair.ordered(
                        endpointPair.source(),
                        target),
                value);
    }
}
