package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.ValueGraph;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public record ImmutableValueTree<V, W>(ImmutableValueGraph<V, W> graph) {

    public static <V, W> ImmutableValueTree<V, W> of(final ValueGraph<V, W> graph) {
        return new ImmutableValueTree<>(ImmutableValueGraph.copyOf(graph));
    }

    public ImmutableValueTree {
        GuavaTreeValidator
                .validateIsTree(graph)
                .throwIfInvalid();
    }

    public V getRootNode() {
        return GuavaGraphs.getRootNode(graph).orElseThrow();
    }
}
