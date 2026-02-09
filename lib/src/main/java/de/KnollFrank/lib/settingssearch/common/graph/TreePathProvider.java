package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.graph.ValueGraph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
class TreePathProvider<N, V, G extends ValueGraph<N, V>> {

    private final Tree<N, V, G> tree;
    private final Map<N, TreePath<N, V, G>> treePathByNode = new HashMap<>();

    public TreePathProvider(final Tree<N, V, G> tree) {
        this.tree = tree;
    }

    public TreePath<N, V, G> getPathFromRootNodeToTarget(final N target) {
        if (!treePathByNode.containsKey(target)) {
            treePathByNode.put(target, calculatePathFromRootNodeToTarget(target));
        }
        return treePathByNode.get(target);
    }

    private TreePath<N, V, G> calculatePathFromRootNodeToTarget(final N target) {
        return tree
                .parentNodeOf(target)
                .map(parentOfTarget ->
                             this
                                     .getPathFromRootNodeToTarget(parentOfTarget)
                                     .add(target))
                .orElseGet(() -> new TreePath<>(tree, List.of(target)));
    }
}
