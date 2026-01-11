package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.collect.ImmutableSet;
import com.google.common.graph.Traverser;

import java.util.Set;

@SuppressWarnings({"UnstableApiUsage"})
public record Subtree<N, V>(Tree<N, V> tree, N rootNodeOfSubtree) {

    public Subtree {
        if (!tree.graph().nodes().contains(rootNodeOfSubtree)) {
            throw new IllegalArgumentException("tree '" + tree + "' must contain rootNodeOfSubtree '" + rootNodeOfSubtree + "'");
        }
    }

    public static <N, V> Subtree<N, V> of(final Tree<N, V> tree) {
        return new Subtree<>(tree, tree.rootNode());
    }

    public Set<N> getSubtreeNodes() {
        return ImmutableSet.copyOf(
                Traverser
                        .forTree(tree.graph())
                        .depthFirstPreOrder(rootNodeOfSubtree));
    }

    public TreeAtNode<N, V> asTreeAtNode() {
        return new TreeAtNode<>(tree, rootNodeOfSubtree);
    }
}
