package de.KnollFrank.lib.settingssearch.common.graph;

import org.jgrapht.Graph;

import java.util.function.Function;

public class NodesTransformer {

    public static <V, W, E> Graph<W, E> transformNodes(
            final Graph<V, E> graph,
            final Function<V, W> transformNode,
            final Class<? extends E> edgeClass,
            final Function<E, E> cloneEdge) {
        return GraphTransformerAlgorithm.transform(
                graph,
                edgeClass,
                transformNodes(transformNode, cloneEdge));
    }

    private static <V, W, E> GraphTransformer<V, E, W, E> transformNodes(
            final Function<V, W> transformNode,
            final Function<E, E> cloneEdge) {
        return new GraphTransformer<>() {

            @Override
            public W transformNode(final V v) {
                return transformNode.apply(v);
            }

            @Override
            public E transformEdge(final E edge, final W w) {
                return cloneEdge.apply(edge);
            }
        };
    }
}
