package de.KnollFrank.lib.settingssearch.graph;

import com.google.common.graph.ImmutableValueGraph;

import de.KnollFrank.lib.settingssearch.common.graph.Tree;

public interface TreeBuilderListener<N, V> {

    void onStartBuildTree(N treeRoot);

    void onStartBuildSubtree(N subtreeRoot, boolean isRootOfTree);

    void onFinishBuildSubtree(N subtreeRoot, boolean isRootOfTree);

    @SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
    void onFinishBuildTree(Tree<N, V, ImmutableValueGraph<N, V>> tree);
}
