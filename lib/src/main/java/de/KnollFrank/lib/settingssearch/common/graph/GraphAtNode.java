package de.KnollFrank.lib.settingssearch.common.graph;

import org.jgrapht.Graph;

import java.util.Set;

public record GraphAtNode<V, E>(
        Graph<V, E> graph,
        V nodeOfGraph) {

    public Set<E> outgoingEdgesOfNodeOfGraph() {
        return graph.outgoingEdgesOf(nodeOfGraph);
    }
}
