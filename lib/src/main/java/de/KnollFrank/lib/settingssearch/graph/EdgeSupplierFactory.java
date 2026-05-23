package de.KnollFrank.lib.settingssearch.graph;

import com.google.common.graph.EndpointPair;

import java.util.Optional;
import java.util.function.Supplier;

import de.KnollFrank.lib.settingssearch.common.graph.Edge;

class EdgeSupplierFactory {

    @SuppressWarnings({"UnstableApiUsage"})
    public static <N, V> EdgeSupplier<N, V> createEdgeSupplier(
            final N source,
            final V edgeValue,
            final Supplier<Optional<N>> childNodeSupplier) {
        return () ->
                childNodeSupplier
                        .get()
                        .map(
                                childNode ->
                                        new Edge<>(
                                                EndpointPair.ordered(source, childNode),
                                                edgeValue));
    }
}
