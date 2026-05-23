package de.KnollFrank.lib.settingssearch.graph;

import java.util.List;
import java.util.stream.Collectors;

public class FilteredEdgeSuppliersFactory<N, V> implements EdgeSuppliersFactory<N, V> {

    private final EdgeSuppliersFactory<N, V> delegate;
    private final AddEdgeToTreePredicate<N, V> addEdgeToTreePredicate;

    public FilteredEdgeSuppliersFactory(final EdgeSuppliersFactory<N, V> delegate,
                                        final AddEdgeToTreePredicate<N, V> addEdgeToTreePredicate) {
        this.delegate = delegate;
        this.addEdgeToTreePredicate = addEdgeToTreePredicate;
    }

    @Override
    public List<EdgeSupplier<N, V>> createEdgeSuppliersHavingSource(final N source) {
        return delegate
                .createEdgeSuppliersHavingSource(source)
                .stream()
                .filter(edgeSupplier ->
                                edgeSupplier
                                        .getEdge()
                                        .map(addEdgeToTreePredicate::shallAddEdgeToTree)
                                        .orElse(false))
                .collect(Collectors.toList());
    }
}
