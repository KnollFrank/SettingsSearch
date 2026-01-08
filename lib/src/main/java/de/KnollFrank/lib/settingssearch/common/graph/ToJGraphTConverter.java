package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.ValueGraph;

import org.jgrapht.Graph;
import org.jgrapht.graph.AsUnmodifiableGraph;
import org.jgrapht.graph.DefaultDirectedGraph;

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
        final Graph<V, E> jgraphtGraph = new DefaultDirectedGraph<>(edgeClass);
        addVertices(jgraphtGraph, guavaGraph.nodes());
        addEdges(guavaGraph, jgraphtGraph);
        return new AsUnmodifiableGraph<>(jgraphtGraph);
    }

    private void addVertices(final Graph<V, E> graph, final Set<V> nodes) {
        nodes.forEach(graph::addVertex);
    }

    private void addEdges(final ValueGraph<V, W> guavaGraph, final Graph<V, E> jgraphtGraph) {
        for (final EndpointPair<V> edge : guavaGraph.edges()) {
            final V source = edge.nodeU();
            final V target = edge.nodeV();
            jgraphtGraph.addEdge(
                    source,
                    target,
                    edgeWrapper.apply(guavaGraph.edgeValueOrDefault(source, target, null)));
        }
    }
}
