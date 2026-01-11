package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.graph.ValueGraph;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class Tree<N, V> extends TypedTree<N, V, ValueGraph<N, V>> {

    public Tree(final ValueGraph<N, V> graph) {
        super(graph);
    }

    public ValueGraph<N, V> graph() {
        return super.graph();
    }

    public TreePath<N, V> getPathFromRootNodeToTarget(final N target) {
        return TreePaths.getPathFromRootNodeToTarget(this, target);
    }
}
