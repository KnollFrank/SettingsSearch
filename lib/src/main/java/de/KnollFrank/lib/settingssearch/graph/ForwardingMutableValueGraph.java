package de.KnollFrank.lib.settingssearch.graph;

import androidx.annotation.Nullable;

import com.google.common.graph.ElementOrder;
import com.google.common.graph.EndpointPair;
import com.google.common.graph.Graph;
import com.google.common.graph.MutableValueGraph;

import java.util.Set;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public abstract class ForwardingMutableValueGraph<N, V> implements MutableValueGraph<N, V> {

    protected abstract MutableValueGraph<N, V> delegate();

    @Override
    public Set<N> nodes() {
        return delegate().nodes();
    }

    @Override
    public Set<EndpointPair<N>> edges() {
        return delegate().edges();
    }

    @Override
    public Graph<N> asGraph() {
        return delegate().asGraph();
    }

    @Override
    public boolean isDirected() {
        return delegate().isDirected();
    }

    @Override
    public boolean allowsSelfLoops() {
        return delegate().allowsSelfLoops();
    }

    @Override
    public ElementOrder<N> nodeOrder() {
        return delegate().nodeOrder();
    }

    @Override
    public ElementOrder<N> incidentEdgeOrder() {
        return delegate().incidentEdgeOrder();
    }

    @Override
    public Set<N> adjacentNodes(N node) {
        return delegate().adjacentNodes(node);
    }

    @Override
    public Set<N> predecessors(N node) {
        return delegate().predecessors(node);
    }

    @Override
    public Set<N> successors(N node) {
        return delegate().successors(node);
    }

    @Override
    public Set<EndpointPair<N>> incidentEdges(N node) {
        return delegate().incidentEdges(node);
    }

    @Override
    public int degree(N node) {
        return delegate().degree(node);
    }

    @Override
    public int inDegree(N node) {
        return delegate().inDegree(node);
    }

    @Override
    public int outDegree(N node) {
        return delegate().outDegree(node);
    }

    @Override
    public boolean hasEdgeConnecting(N nodeU, N nodeV) {
        return delegate().hasEdgeConnecting(nodeU, nodeV);
    }

    @Override
    public boolean hasEdgeConnecting(EndpointPair<N> endpoints) {
        return delegate().hasEdgeConnecting(endpoints);
    }

    @Nullable
    @Override
    public V edgeValueOrDefault(N nodeU, N nodeV, @Nullable V defaultValue) {
        return delegate().edgeValueOrDefault(nodeU, nodeV, defaultValue);
    }

    @Nullable
    @Override
    public V edgeValueOrDefault(EndpointPair<N> endpoints, @Nullable V defaultValue) {
        return delegate().edgeValueOrDefault(endpoints, defaultValue);
    }

    @Override
    public boolean addNode(N node) {
        return delegate().addNode(node);
    }

    @Override
    public V putEdgeValue(N nodeU, N nodeV, V value) {
        return delegate().putEdgeValue(nodeU, nodeV, value);
    }

    @Override
    public V putEdgeValue(EndpointPair<N> endpoints, V value) {
        return delegate().putEdgeValue(endpoints, value);
    }

    @Override
    public boolean removeNode(N node) {
        return delegate().removeNode(node);
    }

    @Override
    public V removeEdge(N nodeU, N nodeV) {
        return delegate().removeEdge(nodeU, nodeV);
    }

    @Override
    public V removeEdge(EndpointPair<N> endpoints) {
        return delegate().removeEdge(endpoints);
    }
}
