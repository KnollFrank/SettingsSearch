package de.KnollFrank.lib.settingssearch.graph;

import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.MutableValueGraph;

import de.KnollFrank.lib.settingssearch.common.graph.Graphs;
import de.KnollFrank.lib.settingssearch.common.graph.Tree;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class TreeBuilder<N, V> {

    private final TreeBuilderListener<N, V> treeBuilderListener;
    private final EdgeSuppliersFactory<N, V> edgeSuppliersFactory;

    public TreeBuilder(final TreeBuilderListener<N, V> treeBuilderListener,
                       final EdgeSuppliersFactory<N, V> edgeSuppliersFactory) {
        this.treeBuilderListener = treeBuilderListener;
        this.edgeSuppliersFactory = edgeSuppliersFactory;
    }

    public Tree<N, V, ImmutableValueGraph<N, V>> buildTreeWithRoot(final N root) {
        treeBuilderListener.onStartBuildTree(root);
        final MutableValueGraph<N, V> graph = Graphs.createEmptyDirectedMutableValueGraph();
        buildGraph(new Node<>(root, true), graph);
        final Tree<N, V, ImmutableValueGraph<N, V>> tree = new Tree<>(ImmutableValueGraph.copyOf(graph));
        treeBuilderListener.onFinishBuildTree(tree);
        return tree;
    }

    private record Node<N>(N node, boolean isRootOfTree) {
    }

    private void buildGraph(final Node<N> root, final MutableValueGraph<N, V> graph) {
        treeBuilderListener.onStartBuildSubtree(root.node(), root.isRootOfTree());
        if (graph.nodes().contains(root.node())) {
            throw new IllegalStateException(
                    String.format(
                            "Cycle detected in the graph. The node '%s' has been visited twice. A tree structure must not contain cycles.",
                            root));
        }
        graph.addNode(root.node());
        for (final EdgeSupplier<N, V> edgeSupplier : edgeSuppliersFactory.createEdgeSuppliersHavingSource(root.node())) {
            edgeSupplier
                    .getEdge()
                    .ifPresent(
                            edge -> {
                                buildGraph(new Node<>(edge.endpointPair().target(), false), graph);
                                Graphs.addEdge(graph, edge);
                            });
        }
        treeBuilderListener.onFinishBuildSubtree(root.node(), root.isRootOfTree());
    }
}
