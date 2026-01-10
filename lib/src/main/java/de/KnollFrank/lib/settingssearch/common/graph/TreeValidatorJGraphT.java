package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.graph.ValueGraph;

import org.jgrapht.Graph;

import java.util.function.Function;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
class TreeValidatorJGraphT {

    public static <V, E> TreeValidatorResult validateIsTree(final Graph<V, E> graph) {
        return TreeValidator.validateIsTree(toGuava(graph));
    }

    private static <Node, Edge> ValueGraph<Node, Edge> toGuava(final Graph<Node, Edge> graph) {
        final var converter = new ToGuavaGraphConverter<Node, Edge, Edge>(Function.identity());
        return converter.toGuava(graph);
    }
}
