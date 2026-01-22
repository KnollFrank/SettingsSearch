package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.graph.ImmutableValueGraph;

import java.util.function.Function;

// FK-TODO: führe eine Klasse SubtreeRemover ein, die einen Teilbaum entfernt?
public class NodeReplacer {

    private NodeReplacer() {
    }

    @SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
    public static <N, V> Tree<N, V, ImmutableValueGraph<N, V>> replaceNode(
            final TreeNode<N, V, ImmutableValueGraph<N, V>> treeNodeToReplace,
            final N replacementNode) {
        return NodesTransformer.transformNodes(
                treeNodeToReplace.tree(),
                createNodeReplacer(treeNodeToReplace.node(), replacementNode));
    }

    private static <V> Function<V, V> createNodeReplacer(final V nodeToReplace, final V replacementNode) {
        return actualNode -> actualNode.equals(nodeToReplace) ? replacementNode : actualNode;
    }
}
