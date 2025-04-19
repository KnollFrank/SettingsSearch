package de.KnollFrank.lib.settingssearch.common.graph;

import java.util.Optional;

public interface GraphTransformer<V1, E1, V2, E2> {

    // FK-TODO: group edgeFromParentNode2Node and transformedParentNode into a new class named NodeContext
    V2 transformNode(V1 node, final Optional<E1> edgeFromParentNode2Node, Optional<V2> transformedParentNode);

    E2 transformEdge(E1 edge, V2 transformedParentNode);
}
