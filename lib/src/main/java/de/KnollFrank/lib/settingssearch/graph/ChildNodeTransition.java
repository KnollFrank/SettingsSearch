package de.KnollFrank.lib.settingssearch.graph;

// FK-TODO: make "V edgeValue" a parameter of ChildNodeProvider.traverse() + source node of transition?
public record ChildNodeTransition<N, V>(V edgeValue, ChildNodeProvider<N> childNodeProvider) {
}
