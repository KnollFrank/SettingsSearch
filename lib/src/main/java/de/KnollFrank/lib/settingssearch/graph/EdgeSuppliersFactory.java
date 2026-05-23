package de.KnollFrank.lib.settingssearch.graph;

import java.util.List;

@FunctionalInterface
public interface EdgeSuppliersFactory<N, V> {

    List<EdgeSupplier<N, V>> createEdgeSuppliersHavingSource(N source);
}
