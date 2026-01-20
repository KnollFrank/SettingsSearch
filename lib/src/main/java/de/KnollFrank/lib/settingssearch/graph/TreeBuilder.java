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
class TreeBuilder<N, V> {

    @FunctionalInterface
    public interface ChildByEdgeValueProvider<N, V> {

        Map<V, N> getChildOfNodeByEdgeValue(N node);
    }

    private final GraphListener<N> graphListener;
    private final ChildByEdgeValueProvider<N, V> childByEdgeValueProvider;
    private final AddEdgeToTreePredicate<N, V> addEdgeToTreePredicate;

    public TreeBuilder(final GraphListener<N> graphListener,
                       final ChildByEdgeValueProvider<N, V> childByEdgeValueProvider,
                       final AddEdgeToTreePredicate<N, V> addEdgeToTreePredicate) {
        this.graphListener = graphListener;
        this.childByEdgeValueProvider = childByEdgeValueProvider;
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
                .getChildOfNodeByEdgeValue(root)
                .forEach(
                        (edgeValue, childOfRoot) -> {
                            buildGraph(childOfRoot, graph);
                            graph.addNode(childOfRoot);
                            graph.putEdgeValue(
                                    EndpointPair.ordered(root, childOfRoot),
                                    edgeValue);
                        });
    }

    private Map<V, N> getChildOfNodeByEdgeValue(final N root) {
        return Maps.filter(
                childByEdgeValueProvider.getChildOfNodeByEdgeValue(root),
                (edgeValue, childOfRoot) ->
                        addEdgeToTreePredicate.shallAddEdgeToTree(
                                new Edge<>(
                                        EndpointPair.ordered(root, childOfRoot),
                                        edgeValue)));
    }
}
