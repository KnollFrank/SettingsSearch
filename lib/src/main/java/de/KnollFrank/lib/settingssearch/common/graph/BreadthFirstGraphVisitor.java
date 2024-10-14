package de.KnollFrank.lib.settingssearch.common.graph;

import org.jgrapht.Graph;
import org.jgrapht.traverse.BreadthFirstIterator;

public abstract class BreadthFirstGraphVisitor<V, E> {

    public void visit(final Graph<V, E> graph) {
        final BreadthFirstIterator<V, E> iterator = new BreadthFirstIterator<>(graph);
        while (iterator.hasNext()) {
            final V node = iterator.next();
            final V parentNode = iterator.getParent(node);
            if (parentNode == null) {
                visitRootNode(node);
            } else {
                visitInnerNode(node, parentNode);
            }
        }
    }

    protected abstract void visitRootNode(final V rootNode);

    protected abstract void visitInnerNode(final V node, final V parentNode);
}
