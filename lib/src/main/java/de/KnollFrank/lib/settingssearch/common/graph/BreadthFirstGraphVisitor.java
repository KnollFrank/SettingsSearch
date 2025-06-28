package de.KnollFrank.lib.settingssearch.common.graph;

import org.jgrapht.Graph;
import org.jgrapht.traverse.BreadthFirstIterator;

import java.util.Optional;

public abstract class BreadthFirstGraphVisitor<V, E> {

    public void visit(final Graph<V, E> graph) {
        final BreadthFirstIterator<V, E> iterator =
                new BreadthFirstIterator<>(
                        graph,
                        GraphUtils.getRootNode(graph).orElseThrow());
        while (iterator.hasNext()) {
            final V node = iterator.next();
            Optional
                    .ofNullable(iterator.getParent(node))
                    .ifPresentOrElse(
                            parentNode -> visitInnerNode(node, parentNode),
                            () -> visitRootNode(node));
        }
    }

    protected abstract void visitRootNode(final V rootNode);

    protected abstract void visitInnerNode(final V innerNode, final V parentNode);
}
