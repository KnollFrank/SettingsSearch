package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.graph.Traverser;

@SuppressWarnings({"UnstableApiUsage"})
public abstract class BreadthFirstTreeVisitor<N, V> {

    public void visit(final Tree<N, V> tree) {
        for (final N node : breadthFirst(tree)) {
            tree
                    .parentNodeOf(node)
                    .ifPresentOrElse(
                            parentNode -> visitInnerNode(node, parentNode),
                            () -> visitRootNode(node));
        }
    }

    protected abstract void visitRootNode(final N rootNode);

    protected abstract void visitInnerNode(final N innerNode, final N parentNode);

    private Iterable<N> breadthFirst(final Tree<N, V> tree) {
        return Traverser
                .forTree(tree.graph())
                .breadthFirst(tree.rootNode());
    }
}
