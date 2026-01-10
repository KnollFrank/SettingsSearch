package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.collect.MoreCollectors;
import com.google.common.graph.ValueGraph;

import java.util.Optional;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public record Tree<N, V>(ValueGraph<N, V> graph) {

    public Tree {
        TreeValidator
                .validateIsTree(graph)
                .throwIfInvalid();
    }

    public N rootNode() {
        return ValueGraphs.getRootNode(graph).orElseThrow();
    }

    public Optional<N> parentNodeOf(final N node) {
        return graph
                .predecessors(node)
                .stream()
                .collect(MoreCollectors.toOptional());
    }
}
