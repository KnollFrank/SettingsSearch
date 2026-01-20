package de.KnollFrank.lib.settingssearch.graph;

import com.google.common.graph.EndpointPair;

import java.util.Map;
import java.util.function.BiPredicate;

import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.common.graph.Edge;

@SuppressWarnings({"UnstableApiUsage"})
public class FilteredChildNodeByEdgeValueProvider<N, V> implements ChildNodeByEdgeValueProvider<N, V> {

    private final ChildNodeByEdgeValueProvider<N, V> delegate;
    private final AddEdgeToTreePredicate<N, V> addEdgeToTreePredicate;

    public FilteredChildNodeByEdgeValueProvider(final ChildNodeByEdgeValueProvider<N, V> delegate,
                                                final AddEdgeToTreePredicate<N, V> addEdgeToTreePredicate) {
        this.delegate = delegate;
        this.addEdgeToTreePredicate = addEdgeToTreePredicate;
    }

    @Override
    public Map<V, N> getChildNodeOfNodeByEdgeValue(final N node) {
        return Maps.filter(
                delegate.getChildNodeOfNodeByEdgeValue(node),
                shallAddEdgeOriginatingFromNodeToTree(node));
    }

    private BiPredicate<V, N> shallAddEdgeOriginatingFromNodeToTree(final N node) {
        return (edgeValue, childNodeOfNode) ->
                addEdgeToTreePredicate.shallAddEdgeToTree(
                        new Edge<>(
                                EndpointPair.ordered(node, childNodeOfNode),
                                edgeValue));
    }
}
