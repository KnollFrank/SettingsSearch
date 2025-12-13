package de.KnollFrank.lib.settingssearch.common.graph;

import org.jgrapht.Graph;

import java.util.function.Function;

public class NodeReplacer<V, E> {

    private final Class<? extends E> edgeClass;
    private final Function<E, E> cloneEdge;

    public NodeReplacer(final Class<? extends E> edgeClass,
                        final Function<E, E> cloneEdge) {
        this.edgeClass = edgeClass;
        this.cloneEdge = cloneEdge;
    }

    public Graph<V, E> replaceNode(final Graph<V, E> originalGraph,
                                   final V nodeOfOriginalGraphToReplace,
                                   final V replacementNode) {
        return NodesTransformer.transformNodes(
                originalGraph,
                createNodeReplacer(nodeOfOriginalGraphToReplace, replacementNode),
                edgeClass,
                cloneEdge);
    }

    private static <V> Function<V, V> createNodeReplacer(final V nodeToReplace, final V replacementNode) {
        return actualNode -> actualNode.equals(nodeToReplace) ? replacementNode : actualNode;
    }
}
