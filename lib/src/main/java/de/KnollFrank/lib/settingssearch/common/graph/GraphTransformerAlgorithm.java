package de.KnollFrank.lib.settingssearch.common.graph;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;

import java.util.HashMap;
import java.util.Map;

import de.KnollFrank.lib.settingssearch.common.graph.GraphTransformer.ContextOfInnerNode;

public class GraphTransformerAlgorithm {

    public static <VSrc, ESrc, VDst, EDst> Graph<VDst, EDst> transform(
            final Graph<VSrc, ESrc> graph,
            final Class<? extends EDst> transformedEdgeClass,
            final GraphTransformer<VSrc, ESrc, VDst, EDst> graphTransformer) {
        final var transformedGraphBuilder = DefaultDirectedGraph.<VDst, EDst>createBuilder(transformedEdgeClass);
        final BreadthFirstGraphVisitor<VSrc, ESrc> graphVisitor =
                new BreadthFirstGraphVisitor<>() {

                    private final Map<VSrc, VDst> transformedNodeByNode = new HashMap<>();

                    @Override
                    protected void visitRootNode(final VSrc rootNode) {
                        final VDst transformedRootNode = graphTransformer.transformRootNode(rootNode);
                        transformedNodeByNode.put(rootNode, transformedRootNode);
                        transformedGraphBuilder.addVertex(transformedRootNode);
                    }

                    @Override
                    protected void visitInnerNode(final VSrc innerNode, final VSrc parentNode) {
                        final VDst transformedParentNode = transformedNodeByNode.get(parentNode);
                        final ESrc edge = graph.getEdge(parentNode, innerNode);
                        final VDst transformedInnerNode =
                                graphTransformer.transformInnerNode(
                                        innerNode,
                                        new ContextOfInnerNode<>(edge, transformedParentNode));
                        transformedNodeByNode.put(innerNode, transformedInnerNode);
                        transformedGraphBuilder.addVertex(transformedInnerNode);
                        transformedGraphBuilder.addEdge(
                                transformedParentNode,
                                transformedInnerNode,
                                graphTransformer.transformEdge(edge, transformedParentNode));
                    }
                };
        graphVisitor.visit(graph);
        return transformedGraphBuilder.build();
    }
}
