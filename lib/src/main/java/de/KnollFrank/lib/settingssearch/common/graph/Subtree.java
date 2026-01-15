package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.collect.ImmutableSet;
import com.google.common.graph.Traverser;
import com.google.common.graph.ValueGraph;

import java.util.Set;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
// FK-TODO: remove, use Tree instead
public record Subtree<N, V, G extends ValueGraph<N, V>>(Tree<N, V, G> tree, N rootNodeOfSubtree) {

    public Subtree {
        if (!tree.graph().nodes().contains(rootNodeOfSubtree)) {
            throw new IllegalArgumentException("tree '" + tree + "' must contain rootNodeOfSubtree '" + rootNodeOfSubtree + "'");
        }
    }

    public static <N, V, G extends ValueGraph<N, V>> Subtree<N, V, G> of(final Tree<N, V, G> tree) {
        return new Subtree<>(tree, tree.rootNode());
    }

    public Set<N> getSubtreeNodes() {
        return ImmutableSet.copyOf(
                Traverser
                        .forTree(tree.graph())
                        .depthFirstPreOrder(rootNodeOfSubtree));
    }

    public TreeAtNode<N, V, G> asTreeAtNode() {
        return new TreeAtNode<>(tree, rootNodeOfSubtree);
    }
}
