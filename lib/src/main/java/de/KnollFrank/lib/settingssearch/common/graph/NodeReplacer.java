package de.KnollFrank.lib.settingssearch.common.graph;

import java.util.function.Function;

public class NodeReplacer {

    public static <N, V> Tree<N, V> replaceNode(final TreeAtNode<N, V> treeAtNodeToReplace,
                                                final N replacementNode) {
        return NodesTransformer.transformNodes(
                treeAtNodeToReplace.tree(),
                createNodeReplacer(treeAtNodeToReplace.nodeOfTree(), replacementNode));
    }

    private static <V> Function<V, V> createNodeReplacer(final V nodeToReplace, final V replacementNode) {
        return actualNode -> actualNode.equals(nodeToReplace) ? replacementNode : actualNode;
    }
}
