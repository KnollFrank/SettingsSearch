package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;

import org.jgrapht.Graph;

import java.util.Set;
import java.util.function.Function;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class ToGuavaGraphConverter {

    public static <V, E, W> ImmutableValueGraph<V, W> toGuava(final Graph<V, E> jgraphtGraph,
                                                              final Function<E, W> edgeValueExtractor) {
        final ImmutableValueGraph.Builder<V, W> graphBuilder = ValueGraphBuilder.directed().immutable();
        addNodes(graphBuilder, jgraphtGraph.vertexSet());
        addEdges(graphBuilder, jgraphtGraph.edgeSet(), jgraphtGraph, edgeValueExtractor);
        return graphBuilder.build();
    }

    private static <V, E, W> void addNodes(final ImmutableValueGraph.Builder<V, W> graphBuilder, final Set<V> nodes) {
        nodes.forEach(graphBuilder::addNode);
    }

    private static <V, E, W> void addEdges(final ImmutableValueGraph.Builder<V, W> graphBuilder,
                                           final Set<E> edges,
                                           final Graph<V, E> jgraphtGraph,
                                           final Function<E, W> edgeValueExtractor) {
        for (final E edge : edges) {
            graphBuilder.putEdgeValue(
                    jgraphtGraph.getEdgeSource(edge),
                    jgraphtGraph.getEdgeTarget(edge),
                    edgeValueExtractor.apply(edge));
        }
    }
}
