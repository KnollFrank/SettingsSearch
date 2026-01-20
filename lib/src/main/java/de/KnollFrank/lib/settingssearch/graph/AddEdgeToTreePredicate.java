package de.KnollFrank.lib.settingssearch.graph;

import de.KnollFrank.lib.settingssearch.common.graph.Edge;

@FunctionalInterface
public interface AddEdgeToTreePredicate<N, V> {

    boolean shallAddEdgeToTree(Edge<N, V> edge);
}
