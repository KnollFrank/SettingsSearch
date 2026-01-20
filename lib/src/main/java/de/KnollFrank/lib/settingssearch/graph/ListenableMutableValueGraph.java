package de.KnollFrank.lib.settingssearch.graph;

import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
class ListenableMutableValueGraph<N, V> extends ForwardingMutableValueGraph<N, V> {

    private final MutableValueGraph<N, V> delegate;
    private final GraphListener<N> listener;

    public ListenableMutableValueGraph(final GraphListener<N> listener) {
        this.delegate = ValueGraphBuilder.directed().build();
        this.listener = listener;
    }

    @Override
    protected MutableValueGraph<N, V> delegate() {
        return delegate;
    }

    @Override
    public boolean addNode(final N node) {
        final boolean added = super.addNode(node);
        if (added) {
            listener.nodeAdded(node);
        }
        return added;
    }
}
