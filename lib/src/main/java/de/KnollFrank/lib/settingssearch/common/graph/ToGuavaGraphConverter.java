package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;

import org.jgrapht.Graph;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class ToGuavaGraphConverter<TNode, TEdge, TEdgeValue> {

    private final Function<TEdge, TEdgeValue> edgeValueExtractor;

    public ToGuavaGraphConverter(final Function<TEdge, TEdgeValue> edgeValueExtractor) {
        this.edgeValueExtractor = edgeValueExtractor;
    }

    public ImmutableValueGraph<TNode, TEdgeValue> toGuava(final Graph<TNode, TEdge> jgraphtGraph) {
        final ImmutableValueGraph.Builder<TNode, TEdgeValue> guavaGraphBuilder = ValueGraphBuilder.directed().immutable();
        addNodes(guavaGraphBuilder, jgraphtGraph.vertexSet());
        addEdges(guavaGraphBuilder, getEdges(jgraphtGraph));
        return guavaGraphBuilder.build();
    }

    private void addNodes(final ImmutableValueGraph.Builder<TNode, TEdgeValue> guavaGraphBuilder, final Set<TNode> nodes) {
        nodes.forEach(guavaGraphBuilder::addNode);
    }

    private Set<Edge<TNode, TEdgeValue>> getEdges(final Graph<TNode, TEdge> jgraphtGraph) {
        return jgraphtGraph
                .edgeSet()
                .stream()
                .map(edge ->
                             new Edge<>(
                                     EndpointPair.ordered(
                                             jgraphtGraph.getEdgeSource(edge),
                                             jgraphtGraph.getEdgeTarget(edge)),
                                     edgeValueExtractor.apply(edge)))
                .collect(Collectors.toSet());
    }

    private void addEdges(final ImmutableValueGraph.Builder<TNode, TEdgeValue> guavaGraphBuilder,
                          final Set<Edge<TNode, TEdgeValue>> edges) {
        edges.forEach(edge -> guavaGraphBuilder.putEdgeValue(edge.edge(), edge.edgeValue()));
    }
}
