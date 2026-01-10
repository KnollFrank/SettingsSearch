package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.graph.ValueGraph;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public record Tree<V, W>(ValueGraph<V, W> graph) {

    public Tree {
        TreeValidator
                .validateIsTree(graph)
                .throwIfInvalid();
    }

    public V getRootNode() {
        return ValueGraphs.getRootNode(graph).orElseThrow();
    }
}
