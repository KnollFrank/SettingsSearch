package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;

import org.jgrapht.Graph;

import java.util.Set;
import java.util.function.Function;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
class ToGuavaGraphConverter<V, E, W> {

    private final Function<E, W> edgeValueExtractor;

    public ToGuavaGraphConverter(final Function<E, W> edgeValueExtractor) {
        this.edgeValueExtractor = edgeValueExtractor;
    }

    public ImmutableValueGraph<V, W> toGuava(final Graph<V, E> jgraphtGraph) {
        final ImmutableValueGraph.Builder<V, W> graphBuilder = ValueGraphBuilder.directed().immutable();
        addNodes(graphBuilder, jgraphtGraph.vertexSet());
        addEdges(graphBuilder, jgraphtGraph.edgeSet(), jgraphtGraph);
        return graphBuilder.build();
    }

    private void addNodes(final ImmutableValueGraph.Builder<V, W> graphBuilder, final Set<V> nodes) {
        nodes.forEach(graphBuilder::addNode);
    }

    private void addEdges(final ImmutableValueGraph.Builder<V, W> graphBuilder,
                          final Set<E> edges,
                          final Graph<V, E> jgraphtGraph) {
        for (final E edge : edges) {
            graphBuilder.putEdgeValue(
                    jgraphtGraph.getEdgeSource(edge),
                    jgraphtGraph.getEdgeTarget(edge),
                    edgeValueExtractor.apply(edge));
        }
    }
}
