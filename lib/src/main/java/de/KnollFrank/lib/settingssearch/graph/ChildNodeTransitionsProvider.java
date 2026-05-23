package de.KnollFrank.lib.settingssearch.graph;

import java.util.List;

@FunctionalInterface
public interface ChildNodeTransitionsProvider<N, V> {

    List<ChildNodeTransition<N, V>> getChildNodeTransitions(N node);
}
