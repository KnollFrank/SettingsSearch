package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.graph.EndpointPair;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public record Edge<N, V>(EndpointPair<N> endpointPair, V value) {
}
