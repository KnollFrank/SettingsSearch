package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;

import org.jgrapht.Graph;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class ToGuavaGraphConverter<Node, Edge, EdgeValue> {

    private final Function<Edge, EdgeValue> edgeValueExtractor;

    public ToGuavaGraphConverter(final Function<Edge, EdgeValue> edgeValueExtractor) {
        this.edgeValueExtractor = edgeValueExtractor;
    }

    public ImmutableValueGraph<Node, EdgeValue> toGuava(final Graph<Node, Edge> jgraphtGraph) {
        final ImmutableValueGraph.Builder<Node, EdgeValue> guavaGraphBuilder = ValueGraphBuilder.directed().immutable();
        addNodes(guavaGraphBuilder, jgraphtGraph.vertexSet());
        addEdges(guavaGraphBuilder, getEdgeDescriptions(jgraphtGraph));
        return guavaGraphBuilder.build();
    }

    private void addNodes(final ImmutableValueGraph.Builder<Node, EdgeValue> guavaGraphBuilder, final Set<Node> nodes) {
        nodes.forEach(guavaGraphBuilder::addNode);
    }

    private class EdgeDescription {

        public final Node nodeU;
        public final Node nodeV;
        public final EdgeValue edgeValue;

        public EdgeDescription(Node nodeU, Node nodeV, EdgeValue edgeValue) {
            this.nodeU = nodeU;
            this.nodeV = nodeV;
            this.edgeValue = edgeValue;
        }
    }

    private Set<EdgeDescription> getEdgeDescriptions(final Graph<Node, Edge> jgraphtGraph) {
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

    private void addEdges(final ImmutableValueGraph.Builder<Node, EdgeValue> guavaGraphBuilder,
                          final Set<EdgeDescription> edgeDescriptions) {
        edgeDescriptions.forEach(
                edgeDescription ->
                        guavaGraphBuilder.putEdgeValue(
                                edgeDescription.nodeU,
                                edgeDescription.nodeV,
                                edgeDescription.edgeValue));
    }
}
