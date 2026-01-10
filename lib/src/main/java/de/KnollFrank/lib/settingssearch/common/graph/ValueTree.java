package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.graph.ValueGraph;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public record ValueTree<V, W>(ValueGraph<V, W> graph) {

    public ValueTree {
        GuavaTreeValidator
                .validateIsTree(graph)
                .throwIfInvalid();
    }

    public V getRootNode() {
        return GuavaGraphs.getRootNode(graph).orElseThrow();
    }
}
