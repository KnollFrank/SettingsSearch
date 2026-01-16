package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.ValueGraph;

import java.util.Set;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public record Subtree<N, V, G extends ValueGraph<N, V>>(Tree<N, V, G> tree, N rootNodeOfSubtree) {

    public Subtree {
        if (!tree.graph().nodes().contains(rootNodeOfSubtree)) {
            throw new IllegalArgumentException("tree '" + tree + "' must contain rootNodeOfSubtree '" + rootNodeOfSubtree + "'");
        }
    }

    public static <N, V, G extends ValueGraph<N, V>> Subtree<N, V, G> of(final Tree<N, V, G> tree) {
        return new Subtree<>(tree, tree.rootNode());
    }

    public TreeAtNode<N, V, G> asTreeAtNode() {
        return new TreeAtNode<>(tree, rootNodeOfSubtree);
    }

    public Tree<N, V, ImmutableValueGraph<N, V>> asTree() {
        return new Tree<>(
                ImmutableValueGraph.copyOf(
                        com.google.common.graph.Graphs.inducedSubgraph(
                                tree.graph(),
                                getSubtreeNodes())));
    }

    private Set<N> getSubtreeNodes() {
        return com.google.common.graph.Graphs.reachableNodes(
                tree.graph().asGraph(),
                rootNodeOfSubtree);
    }
}
