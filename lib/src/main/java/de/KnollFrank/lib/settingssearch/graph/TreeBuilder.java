package de.KnollFrank.lib.settingssearch.graph;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.MutableValueGraph;

import java.util.Map;

import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.common.graph.Edge;
import de.KnollFrank.lib.settingssearch.common.graph.Tree;

// FK-TODO: add unit test
@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class TreeBuilder<N, V> {

    @FunctionalInterface
    public interface ChildNodeByEdgeValueProvider<N, V> {

        Map<V, N> getChildNodeOfNodeByEdgeValue(N node);
    }

    private final GraphListener<N> graphListener;
    private final ChildNodeByEdgeValueProvider<N, V> childNodeByEdgeValueProvider;
    private final AddEdgeToTreePredicate<N, V> addEdgeToTreePredicate;

    public TreeBuilder(final GraphListener<N> graphListener,
                       final ChildNodeByEdgeValueProvider<N, V> childNodeByEdgeValueProvider,
                       final AddEdgeToTreePredicate<N, V> addEdgeToTreePredicate) {
        this.graphListener = graphListener;
        this.childNodeByEdgeValueProvider = childNodeByEdgeValueProvider;
        this.addEdgeToTreePredicate = addEdgeToTreePredicate;
    }

    public Tree<N, V, ImmutableValueGraph<N, V>> buildTreeWithRoot(final N root) {
        final MutableValueGraph<N, V> graph = GraphFactory.createEmptyGraph(graphListener);
        buildGraph(root, graph);
        return new Tree<>(ImmutableValueGraph.copyOf(graph));
    }

    private void buildGraph(final N root, final MutableValueGraph<N, V> graph) {
        if (graph.nodes().contains(root)) {
            throw new IllegalStateException(
                    String.format(
                            "Cycle detected in the graph. The node '%s' has been visited twice. A tree structure must not contain cycles.",
                            root));
        }
        graph.addNode(root);
        this
                .getChildNodeOfNodeByEdgeValue(root)
                .forEach(
                        (edgeValue, childNodeOfRoot) -> {
                            buildGraph(childNodeOfRoot, graph);
                            graph.putEdgeValue(
                                    EndpointPair.ordered(root, childNodeOfRoot),
                                    edgeValue);
                        });
    }

    private Map<V, N> getChildNodeOfNodeByEdgeValue(final N root) {
        return Maps.filter(
                childNodeByEdgeValueProvider.getChildNodeOfNodeByEdgeValue(root),
                (edgeValue, childNodeOfRoot) ->
                        addEdgeToTreePredicate.shallAddEdgeToTree(
                                new Edge<>(
                                        EndpointPair.ordered(root, childNodeOfRoot),
                                        edgeValue)));
    }
}
