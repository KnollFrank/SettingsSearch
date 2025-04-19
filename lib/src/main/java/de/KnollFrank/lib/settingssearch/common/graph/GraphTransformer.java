package de.KnollFrank.lib.settingssearch.common.graph;

public interface GraphTransformer<V1, E1, V2, E2> {

    record ContextOfInnerNode<E1, V2>(E1 edgeFromParentNode2InnerNode,
                                      V2 transformedParentNode) {
    }

    V2 transformRootNode(V1 rootNode);

    V2 transformInnerNode(V1 innerNode, ContextOfInnerNode<E1, V2> contextOfInnerNode);

    E2 transformEdge(E1 edge, V2 transformedParentNode);
}
