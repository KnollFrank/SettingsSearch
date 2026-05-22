package de.KnollFrank.lib.settingssearch.graph;

@FunctionalInterface
public interface ChildNodeTransitionsProvider<N, V> {

    Iterable<ChildNodeTransition<N, V>> getChildNodeTransitions(N node);
}
