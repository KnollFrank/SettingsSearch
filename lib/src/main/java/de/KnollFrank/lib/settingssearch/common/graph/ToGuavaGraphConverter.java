package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;

import org.jgrapht.Graph;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
class ToGuavaGraphConverter<V, E, W> {

    private final Function<E, W> edgeValueExtractor;

    public ToGuavaGraphConverter(final Function<E, W> edgeValueExtractor) {
        this.edgeValueExtractor = edgeValueExtractor;
    }

    public ImmutableValueGraph<V, W> toGuava(final Graph<V, E> jgraphtGraph) {
        final ImmutableValueGraph.Builder<V, W> guavaGraphBuilder = ValueGraphBuilder.directed().immutable();
        addNodes(guavaGraphBuilder, jgraphtGraph.vertexSet());
        addEdges(guavaGraphBuilder, getEdgeDescriptions(jgraphtGraph));
        return guavaGraphBuilder.build();
    }

    private void addNodes(final ImmutableValueGraph.Builder<V, W> guavaGraphBuilder, final Set<V> nodes) {
        nodes.forEach(guavaGraphBuilder::addNode);
    }

    private class EdgeDescription {

        public final V nodeU;
        public final V nodeV;
        public final W value;

        public EdgeDescription(V nodeU, V nodeV, W value) {
            this.nodeU = nodeU;
            this.nodeV = nodeV;
            this.value = value;
        }
    }

    private Set<EdgeDescription> getEdgeDescriptions(final Graph<V, E> jgraphtGraph) {
        return jgraphtGraph
                .edgeSet()
                .stream()
                .map(edge ->
                             new EdgeDescription(
                                     jgraphtGraph.getEdgeSource(edge),
                                     jgraphtGraph.getEdgeTarget(edge),
                                     edgeValueExtractor.apply(edge)))
                .collect(Collectors.toSet());
    }

    private void addEdges(final ImmutableValueGraph.Builder<V, W> guavaGraphBuilder,
                          final Set<EdgeDescription> edgeDescriptions) {
        edgeDescriptions.forEach(
                edgeDescription ->
                        guavaGraphBuilder.putEdgeValue(
                                edgeDescription.nodeU,
                                edgeDescription.nodeV,
                                edgeDescription.value));
    }
}
