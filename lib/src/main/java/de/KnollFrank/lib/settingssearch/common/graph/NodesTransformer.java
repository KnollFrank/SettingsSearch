package de.KnollFrank.lib.settingssearch.common.graph;

import org.jgrapht.Graph;

import java.util.function.Function;

public class NodesTransformer {

    public static <VSrc, VDst, E> Graph<VDst, E> transformNodes(
            final Graph<VSrc, E> graph,
            final Function<VSrc, VDst> transformNode,
            final Class<? extends E> edgeClass,
            final Function<E, E> cloneEdge) {
        return GraphTransformerAlgorithm.transform(
                graph,
                edgeClass,
                transformNodes(transformNode, cloneEdge));
    }

    private static <VSrc, VDst, E> GraphTransformer<VSrc, E, VDst, E> transformNodes(
            final Function<VSrc, VDst> transformNode,
            final Function<E, E> cloneEdge) {
        return new GraphTransformer<>() {

            @Override
            public VDst transformRootNode(final VSrc rootNode) {
                return transformNode.apply(rootNode);
            }

            @Override
            public VDst transformInnerNode(final VSrc innerNode, final ContextOfInnerNode<E, VDst> contextOfInnerNode) {
                return transformNode.apply(innerNode);
            }

            @Override
            public E transformEdge(final E edge, final VDst transformedParentNode) {
                return cloneEdge.apply(edge);
            }
        };
    }
}
