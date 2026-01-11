package de.KnollFrank.lib.settingssearch.common.graph;

import java.util.function.Function;

public class NodesTransformer {

    public static <NSrc, NDst, V> Tree<NDst, V> transformNodes(
            final Tree<NSrc, V> tree,
            final Function<NSrc, NDst> transformNode) {
        return TreeTransformerAlgorithm.transform(
                tree,
                transformNodes(transformNode));
    }

    private static <VSrc, VDst, E> TreeTransformer<VSrc, E, VDst, E> transformNodes(
            final Function<VSrc, VDst> transformNode) {
        return new TreeTransformer<>() {

            @Override
            public VDst transformRootNode(final VSrc rootNode) {
                return transformNode.apply(rootNode);
            }

            @Override
            public VDst transformInnerNode(final VSrc innerNode, final ContextOfInnerNode<E, VDst> contextOfInnerNode) {
                return transformNode.apply(innerNode);
            }

            @Override
            public E transformEdgeValue(final E edgeValue, final VDst transformedParentNode) {
                return edgeValue;
            }
        };
    }
}
