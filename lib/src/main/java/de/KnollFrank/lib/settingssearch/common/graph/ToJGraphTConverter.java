package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.ValueGraph;

import org.jgrapht.graph.AsUnmodifiableGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.builder.GraphBuilder;

import java.util.Set;
import java.util.function.Function;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
class ToJGraphTConverter<V, E, W> {

    private final Class<E> edgeClass;
    private final Function<W, E> edgeWrapper;

    public ToJGraphTConverter(final Class<E> edgeClass, final Function<W, E> edgeWrapper) {
        this.edgeClass = edgeClass;
        this.edgeWrapper = edgeWrapper;
    }

    public AsUnmodifiableGraph<V, E> toJGraphT(final ValueGraph<V, W> guavaGraph) {
        final GraphBuilder<V, E, ? extends DefaultDirectedGraph<V, E>> jgraphtGraphBuilder = DefaultDirectedGraph.createBuilder(edgeClass);
        addVertices(jgraphtGraphBuilder, guavaGraph.nodes());
        addEdges(jgraphtGraphBuilder, guavaGraph);
        return new AsUnmodifiableGraph<>(jgraphtGraphBuilder.build());
    }

    private void addVertices(final GraphBuilder<V, E, ? extends DefaultDirectedGraph<V, E>> jgraphtGraphBuilder,
                             final Set<V> vertices) {
        vertices.forEach(jgraphtGraphBuilder::addVertex);
    }

    private void addEdges(final GraphBuilder<V, E, ? extends DefaultDirectedGraph<V, E>> jgraphtGraphBuilder, final ValueGraph<V, W> guavaGraph) {
        for (final EndpointPair<V> edge : guavaGraph.edges()) {
            final V source = edge.nodeU();
            final V target = edge.nodeV();
            jgraphtGraphBuilder.addEdge(
                    source,
                    target,
                    edgeWrapper.apply(guavaGraph.edgeValueOrDefault(source, target, null)));
        }
    }
}
