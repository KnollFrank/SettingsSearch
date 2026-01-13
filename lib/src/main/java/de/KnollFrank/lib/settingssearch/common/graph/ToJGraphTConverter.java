package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.ImmutableValueGraph;

import org.jgrapht.graph.AsUnmodifiableGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.builder.GraphBuilder;

import java.util.Set;
import java.util.function.Function;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class ToJGraphTConverter<Node, Edge, EdgeValue> {

    private final Class<Edge> edgeClass;
    private final Function<EdgeValue, Edge> edgeWrapper;

    public ToJGraphTConverter(final Class<Edge> edgeClass, final Function<EdgeValue, Edge> edgeWrapper) {
        this.edgeClass = edgeClass;
        this.edgeWrapper = edgeWrapper;
    }

    public AsUnmodifiableGraph<Node, Edge> toJGraphT(final ImmutableValueGraph<Node, EdgeValue> guavaGraph) {
        final GraphBuilder<Node, Edge, ? extends DefaultDirectedGraph<Node, Edge>> jgraphtGraphBuilder = DefaultDirectedGraph.createBuilder(edgeClass);
        addVertices(jgraphtGraphBuilder, guavaGraph.nodes());
        addEdges(jgraphtGraphBuilder, guavaGraph);
        return new AsUnmodifiableGraph<>(jgraphtGraphBuilder.build());
    }

    private void addVertices(final GraphBuilder<Node, Edge, ? extends DefaultDirectedGraph<Node, Edge>> jgraphtGraphBuilder,
                             final Set<Node> vertices) {
        vertices.forEach(jgraphtGraphBuilder::addVertex);
    }

    private void addEdges(final GraphBuilder<Node, Edge, ? extends DefaultDirectedGraph<Node, Edge>> jgraphtGraphBuilder, final ImmutableValueGraph<Node, EdgeValue> guavaGraph) {
        for (final EndpointPair<Node> edge : guavaGraph.edges()) {
            final Node source = edge.nodeU();
            final Node target = edge.nodeV();
            jgraphtGraphBuilder.addEdge(
                    source,
                    target,
                    edgeWrapper.apply(guavaGraph.edgeValueOrDefault(source, target, null)));
        }
    }
}
