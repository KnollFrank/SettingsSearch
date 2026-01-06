package de.KnollFrank.lib.settingssearch.common.graph;

import org.jgrapht.Graph;
import org.jgrapht.graph.AsUnmodifiableGraph;

public record Tree<V, E>(AsUnmodifiableGraph<V, E> graph) {

    public static <V, E> Tree<V, E> of(final Graph<V, E> graph) {
        return new Tree<>(new AsUnmodifiableGraph<>(graph));
    }

    public Tree {
        TreeValidator
                .validateIsTree(graph)
                .throwIfInvalid();
    }

    public V getRootNode() {
        return Graphs
                .getRootNode(graph)
                .orElseThrow();
    }
}
