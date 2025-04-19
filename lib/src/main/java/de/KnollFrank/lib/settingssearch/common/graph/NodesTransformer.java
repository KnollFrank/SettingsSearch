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
            public W transformRootNode(final V rootNode) {
                return transformNode.apply(rootNode);
            }

            @Override
            public W transformInnerNode(final V innerNode, final ContextOfInnerNode<E, W> contextOfInnerNode) {
                return transformNode.apply(innerNode);
            }

            @Override
            public E transformEdge(final E edge, final W transformedParentNode) {
                return cloneEdge.apply(edge);
            }
        };
    }
}
