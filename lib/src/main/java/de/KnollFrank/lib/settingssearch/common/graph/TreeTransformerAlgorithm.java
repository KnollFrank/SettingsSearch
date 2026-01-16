package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.ImmutableValueGraph;

import java.util.HashMap;
import java.util.Map;

import de.KnollFrank.lib.settingssearch.common.graph.TreeTransformer.ContextOfInnerNode;

public class TreeTransformerAlgorithm {

    @SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
    public static <NSrc, VSrc, NDst, VDst> Tree<NDst, VDst, ImmutableValueGraph<NDst, VDst>> transform(
            final Tree<NSrc, VSrc, ImmutableValueGraph<NSrc, VSrc>> tree,
            final TreeTransformer<NSrc, VSrc, NDst, VDst> treeTransformer) {
        final ImmutableValueGraph.Builder<NDst, VDst> transformedGraphBuilder = Graphs.directedImmutableValueGraphBuilder();
        final BreadthFirstTreeVisitor<NSrc, VSrc> treeVisitor =
                new BreadthFirstTreeVisitor<>() {

                    private final Map<NSrc, NDst> transformedNodeByNode = new HashMap<>();

                    @Override
                    protected void visitRootNode(final NSrc rootNode) {
                        final NDst transformedRootNode = treeTransformer.transformRootNode(rootNode);
                        transformedNodeByNode.put(rootNode, transformedRootNode);
                        transformedGraphBuilder.addNode(transformedRootNode);
                    }

                    @Override
                    protected void visitInnerNode(final NSrc innerNode, final NSrc parentNode) {
                        final NDst transformedParentNode = transformedNodeByNode.get(parentNode);
                        final VSrc edgeValue =
                                Graphs.getEdgeValue(
                                        EndpointPair.ordered(parentNode, innerNode),
                                        tree.graph());
                        final NDst transformedInnerNode =
                                treeTransformer.transformInnerNode(
                                        innerNode,
                                        new ContextOfInnerNode<>(edgeValue, transformedParentNode));
                        transformedNodeByNode.put(innerNode, transformedInnerNode);
                        transformedGraphBuilder.addNode(transformedInnerNode);
                        transformedGraphBuilder.putEdgeValue(
                                transformedParentNode,
                                transformedInnerNode,
                                treeTransformer.transformEdgeValue(edgeValue, transformedParentNode));
                    }
                };
        treeVisitor.visit(tree);
        return new Tree<>(transformedGraphBuilder.build());
    }
}
