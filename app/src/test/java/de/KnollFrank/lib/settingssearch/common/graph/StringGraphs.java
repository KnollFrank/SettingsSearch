package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
class StringGraphs {

    // FK-TODO: use newStringGraphBuilder() where possible in tests
    public static ImmutableValueGraph.Builder<StringNode, String> newStringGraphBuilder() {
        return ValueGraphBuilder.directed().immutable();
    }
}
