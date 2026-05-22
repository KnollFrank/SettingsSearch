package de.KnollFrank.lib.settingssearch.graph;

import com.google.common.graph.EndpointPair;

import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import de.KnollFrank.lib.settingssearch.common.graph.Edge;

@SuppressWarnings({"UnstableApiUsage"})
public class FilteredChildNodeTransitionsProvider<N, V> implements ChildNodeTransitionsProvider<N, V> {

    private final ChildNodeTransitionsProvider<N, V> delegate;
    private final AddEdgeToTreePredicate<N, V> addEdgeToTreePredicate;

    public FilteredChildNodeTransitionsProvider(final ChildNodeTransitionsProvider<N, V> delegate,
                                                final AddEdgeToTreePredicate<N, V> addEdgeToTreePredicate) {
        this.delegate = delegate;
        this.addEdgeToTreePredicate = addEdgeToTreePredicate;
    }

    @Override
    public Iterable<ChildNodeTransition<N, V>> getChildNodeTransitions(final N node) {
        return StreamSupport
                .stream(delegate.getChildNodeTransitions(node).spliterator(), false)
                .filter(childNodeTransition -> shallAddEdgeOriginatingFromNodeToTree(node).test(childNodeTransition.edgeValue(), childNodeTransition.childNode()))
                .collect(Collectors.toList());
    }

    // FK-TODO: return Predicate<ChildNodeTransition<N, V>>
    private BiPredicate<V, N> shallAddEdgeOriginatingFromNodeToTree(final N node) {
        return (edgeValue, childNodeOfNode) ->
                addEdgeToTreePredicate.shallAddEdgeToTree(
                        new Edge<>(
                                EndpointPair.ordered(node, childNodeOfNode),
                                edgeValue));
    }
}
