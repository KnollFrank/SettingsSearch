package de.KnollFrank.lib.settingssearch.common.graph;

import org.jgrapht.Graph;
import org.jgrapht.graph.AsUnmodifiableGraph;

public record UnmodifiableTree<V, E>(AsUnmodifiableGraph<V, E> graph) {

    public static <V, E> UnmodifiableTree<V, E> of(final Graph<V, E> graph) {
        return new UnmodifiableTree<>(new AsUnmodifiableGraph<>(graph));
    }

    public UnmodifiableTree {
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
