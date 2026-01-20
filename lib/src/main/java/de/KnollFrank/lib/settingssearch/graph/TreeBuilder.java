package de.KnollFrank.lib.settingssearch.graph;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.MutableValueGraph;

import de.KnollFrank.lib.settingssearch.common.graph.Graphs;
import de.KnollFrank.lib.settingssearch.common.graph.Tree;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class TreeBuilder<N, V> {

    private final TreeBuilderListener<N> treeBuilderListener;
    private final ChildNodeByEdgeValueProvider<N, V> childNodeByEdgeValueProvider;

    public TreeBuilder(final TreeBuilderListener<N> treeBuilderListener,
                       final ChildNodeByEdgeValueProvider<N, V> childNodeByEdgeValueProvider) {
        this.treeBuilderListener = treeBuilderListener;
        this.childNodeByEdgeValueProvider = childNodeByEdgeValueProvider;
    }

    public Tree<N, V, ImmutableValueGraph<N, V>> buildTreeWithRoot(final N root) {
        final MutableValueGraph<N, V> graph = Graphs.createEmptyDirectedMutableValueGraph();
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
        treeBuilderListener.onBuildSubtreeStarted(root);
        graph.addNode(root);
        childNodeByEdgeValueProvider
                .getChildNodeOfNodeByEdgeValue(root)
                .forEach(
                        (edgeValue, childNodeOfRoot) -> {
                            buildGraph(childNodeOfRoot, graph);
                            graph.putEdgeValue(
                                    EndpointPair.ordered(root, childNodeOfRoot),
                                    edgeValue);
                        });
        treeBuilderListener.onBuildSubtreeFinished(root);
    }
}
