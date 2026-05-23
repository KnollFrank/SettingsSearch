package de.KnollFrank.lib.settingssearch.graph;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.common.graph.Edge;

@FunctionalInterface
public interface EdgeSupplier<N, V> {

    Optional<Edge<N, V>> getEdge();
}
