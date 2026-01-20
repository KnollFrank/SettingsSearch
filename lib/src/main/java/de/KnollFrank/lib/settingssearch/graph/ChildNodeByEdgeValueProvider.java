package de.KnollFrank.lib.settingssearch.graph;

import java.util.Map;

@FunctionalInterface
public interface ChildNodeByEdgeValueProvider<N, V> {

    Map<V, N> getChildNodeOfNodeByEdgeValue(N node);
}
