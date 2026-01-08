package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;

import org.jgrapht.Graph;
import org.jgrapht.graph.AsUnmodifiableGraph;
import org.jgrapht.graph.DefaultDirectedGraph;

import java.util.function.Function;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class GraphConverters {

    public static <V, E, W> ImmutableValueGraph<V, W> toGuava(final Graph<V, E> jgraphtGraph,
                                                              final Function<E, W> edgeValueExtractor) {
        // FK-TODO: refactor to usage of ValueGraphBuilder.directed().immutable()
        final MutableValueGraph<V, W> mutableGraph = ValueGraphBuilder.directed().build();
        jgraphtGraph.vertexSet().forEach(mutableGraph::addNode);
        for (final E edge : jgraphtGraph.edgeSet()) {
            mutableGraph.putEdgeValue(
                    jgraphtGraph.getEdgeSource(edge),
                    jgraphtGraph.getEdgeTarget(edge),
                    edgeValueExtractor.apply(edge));
        }
        return ImmutableValueGraph.copyOf(mutableGraph);
    }

    public static <V, E, W> AsUnmodifiableGraph<V, E> asJGraphT(final ImmutableValueGraph<V, W> guavaGraph,
                                                                final Class<E> edgeClass,
                                                                final Function<W, E> edgeWrapper) {
        final Graph<V, E> jgraphtGraph = new DefaultDirectedGraph<>(edgeClass);
        guavaGraph.nodes().forEach(jgraphtGraph::addVertex);
        for (final EndpointPair<V> edge : guavaGraph.edges()) {
            final V source = edge.nodeU();
            final V target = edge.nodeV();
            jgraphtGraph.addEdge(
                    source,
                    target,
                    edgeWrapper.apply(guavaGraph.edgeValueOrDefault(source, target, null)));
        }
        return new AsUnmodifiableGraph<>(jgraphtGraph);
    }
}
