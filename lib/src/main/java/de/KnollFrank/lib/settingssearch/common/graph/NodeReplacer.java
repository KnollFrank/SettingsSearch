package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.graph.ImmutableValueGraph;

import java.util.function.Function;

public class NodeReplacer {

    @SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
    public static <N, V> Tree<N, V, ImmutableValueGraph<N, V>> replaceNode(final TreeAtNode<N, V, ImmutableValueGraph<N, V>> treeAtNodeToReplace,
                                                                           final N replacementNode) {
        return NodesTransformer.transformNodes(
                treeAtNodeToReplace.tree(),
                createNodeReplacer(treeAtNodeToReplace.nodeOfTree(), replacementNode));
    }

    private static <V> Function<V, V> createNodeReplacer(final V nodeToReplace, final V replacementNode) {
        return actualNode -> actualNode.equals(nodeToReplace) ? replacementNode : actualNode;
    }
}
