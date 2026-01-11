package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.graph.ValueGraph;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class TreeAtNode<N, V> extends TypedTreeAtNode<N, V, ValueGraph<N, V>, Tree<N, V>> {

    public TreeAtNode(final Tree<N, V> tree, final N nodeOfTree) {
        super(tree, nodeOfTree);
    }

    @Override
    public Tree<N, V> tree() {
        return super.tree();
    }
}
