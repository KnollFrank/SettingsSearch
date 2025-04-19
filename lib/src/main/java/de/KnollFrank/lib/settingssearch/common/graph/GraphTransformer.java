package de.KnollFrank.lib.settingssearch.common.graph;

public interface GraphTransformer<V1, E1, V2, E2> {

    record NodeContext<E1, V2>(E1 edgeFromParentNode2Node,
                               V2 transformedParentNode) {
    }

    V2 transformRootNode(V1 node);

    V2 transformInnerNode(V1 node, NodeContext<E1, V2> nodeContext);

    E2 transformEdge(E1 edge, V2 transformedParentNode);
}
