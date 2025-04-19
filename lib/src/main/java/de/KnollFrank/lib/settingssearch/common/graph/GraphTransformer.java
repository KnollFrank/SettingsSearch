package de.KnollFrank.lib.settingssearch.common.graph;

import java.util.Optional;

public interface GraphTransformer<V1, E1, V2, E2> {

    V2 transformNode(V1 node, Optional<V2> transformedParentNode);

    E2 transformEdge(E1 edge, V2 transformedParentNode);
}
