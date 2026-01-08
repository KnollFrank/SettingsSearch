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
        final ImmutableValueGraph.Builder<V, W> guavaGraphBuilder = ValueGraphBuilder.directed().immutable();
        addNodes(guavaGraphBuilder, jgraphtGraph.vertexSet());
        addEdges(guavaGraphBuilder, jgraphtGraph.edgeSet(), jgraphtGraph);
        return guavaGraphBuilder.build();
    }

    private void addNodes(final ImmutableValueGraph.Builder<V, W> guavaGraphBuilder, final Set<V> nodes) {
        nodes.forEach(guavaGraphBuilder::addNode);
    }

    private void addEdges(final ImmutableValueGraph.Builder<V, W> guavaGraphBuilder,
                          final Set<E> edges,
                          final Graph<V, E> jgraphtGraph) {
        for (final E edge : edges) {
            guavaGraphBuilder.putEdgeValue(
                    jgraphtGraph.getEdgeSource(edge),
                    jgraphtGraph.getEdgeTarget(edge),
                    edgeValueExtractor.apply(edge));
        }
    }
}
