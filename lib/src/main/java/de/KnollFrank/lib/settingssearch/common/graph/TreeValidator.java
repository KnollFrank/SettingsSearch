package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.graph.ValueGraph;

import org.jgrapht.Graph;

import java.util.function.Function;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
class TreeValidator {

    public static <V, E> TreeValidatorResult validateIsTree(final Graph<V, E> graph) {
        return GuavaTreeValidator.validateIsTree(toGuava(graph));
    }

    private static <V, E> ValueGraph<V, E> toGuava(final Graph<V, E> graph) {
        final var converter = new ToGuavaGraphConverter<V, E, E>(Function.identity());
        return converter.toGuava(graph);
    }
}
