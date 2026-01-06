package de.KnollFrank.lib.settingssearch.common.graph;

import org.jgrapht.Graph;

public record Tree<V, E>(Graph<V, E> graph) {

    public Tree {
        TreeValidator
                .validateIsTree(graph)
                .throwIfInvalid();
    }

    public V getRoot() {
        return Graphs
                .getRootNode(graph)
                .orElseThrow();
    }
}
