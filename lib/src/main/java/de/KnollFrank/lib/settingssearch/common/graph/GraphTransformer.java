package de.KnollFrank.lib.settingssearch.common.graph;

import java.util.Optional;

public interface GraphTransformer<V1, E1, V2, E2> {

    record NodeContext<E1, V2>(E1 edgeFromParentNode2Node,
                               V2 transformedParentNode) {
    }

    V2 transformNode(V1 node, Optional<NodeContext<E1, V2>> nodeContext);

    E2 transformEdge(E1 edge, V2 transformedParentNode);
}
