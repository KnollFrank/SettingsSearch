package de.KnollFrank.lib.settingssearch.common.graph;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GraphTransformerAlgorithm {

    public static <V1, E1, V2, E2> Graph<V2, E2> transform(
            final Graph<V1, E1> graph,
            final Class<? extends E2> transformedEdgeClass,
            final GraphTransformer<V1, E1, V2, E2> graphTransformer) {
        final Graph<V2, E2> transformedGraph = new DefaultDirectedGraph<>(transformedEdgeClass);
        final BreadthFirstGraphVisitor<V1, E1> graphVisitor =
                new BreadthFirstGraphVisitor<>() {

                    private final Map<V1, V2> transformedNodeByNode = new HashMap<>();

                    @Override
                    protected void visitRootNode(final V1 rootNode) {
                        final V2 transformedRootNode = graphTransformer.transformNode(rootNode, Optional.empty(), Optional.empty());
                        transformedNodeByNode.put(rootNode, transformedRootNode);
                        transformedGraph.addVertex(transformedRootNode);
                    }

                    @Override
                    protected void visitInnerNode(final V1 node, final V1 parentNode) {
                        final V2 transformedParentNode = transformedNodeByNode.get(parentNode);
                        final E1 edge = graph.getEdge(parentNode, node);
                        final V2 transformedNode = graphTransformer.transformNode(node, Optional.of(edge), Optional.of(transformedParentNode));
                        transformedNodeByNode.put(node, transformedNode);
                        transformedGraph.addVertex(transformedNode);
                        transformedGraph.addEdge(
                                transformedParentNode,
                                transformedNode,
                                graphTransformer.transformEdge(edge, transformedParentNode));
                    }
                };
        graphVisitor.visit(graph);
        return transformedGraph;
    }
}
