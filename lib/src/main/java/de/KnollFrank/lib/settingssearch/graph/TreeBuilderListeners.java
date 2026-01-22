package de.KnollFrank.lib.settingssearch.graph;

import com.google.common.graph.ImmutableValueGraph;

import de.KnollFrank.lib.settingssearch.common.graph.Tree;

public class TreeBuilderListeners {

    private TreeBuilderListeners() {
    }

    public static <N, V> TreeBuilderListener<N, V> emptyTreeBuilderListener() {
        return new TreeBuilderListener<>() {

            @Override
            public void onStartBuildTree(final N treeRoot) {
            }

            @Override
            public void onStartBuildSubtree(final N subtreeRoot) {
            }

            @Override
            public void onFinishBuildSubtree(final N subtreeRoot) {
            }

            @Override
            @SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
            public void onFinishBuildTree(final Tree<N, V, ImmutableValueGraph<N, V>> tree) {
            }
        };
    }
}
