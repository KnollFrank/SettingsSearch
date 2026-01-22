package de.KnollFrank.lib.settingssearch.graph;

import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.common.Sets;
import de.KnollFrank.lib.settingssearch.common.graph.Edge;
import de.KnollFrank.lib.settingssearch.common.graph.Graphs;
import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.common.graph.TreeNode;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class TreeMerger {

    private TreeMerger() {
    }

    public static <N, V> Tree<N, V, ImmutableValueGraph<N, V>> mergeTreeIntoTreeNode(
            final Tree<N, V, ImmutableValueGraph<N, V>> tree,
            final TreeNode<N, V, ImmutableValueGraph<N, V>> treeNode) {
        assertRootNodeMatchesTreeNode(tree, treeNode);
        assertNodesDoNotOverlap(tree, treeNode);

        final MutableValueGraph<N, V> mergedGraph = createEmptyGraph(treeNode);
        mergeTreeIntoTreeNode(tree, treeNode, mergedGraph);
        return new Tree<>(ImmutableValueGraph.copyOf(mergedGraph));
    }

    private static <N, V> void assertRootNodeMatchesTreeNode(
            final Tree<N, V, ImmutableValueGraph<N, V>> tree,
            final TreeNode<N, V, ImmutableValueGraph<N, V>> treeNode) {
        if (!Objects.equals(tree.rootNode(), treeNode.node())) {
            throw new IllegalArgumentException("root node of tree " + tree.rootNode() + " does not match tree node " + treeNode.node());
        }
    }

    private static <N, V> void assertNodesDoNotOverlap(
            final Tree<N, V, ImmutableValueGraph<N, V>> tree,
            final TreeNode<N, V, ImmutableValueGraph<N, V>> treeNode) {
        final Set<N> overlappingNodes =
                Sets.intersection(
                        getNodesWithoutRootNode(tree),
                        getNodesWithoutRootNode(treeNode.tree()));
        if (!overlappingNodes.isEmpty()) {
            throw new IllegalArgumentException("Merge would result in an invalid tree. The following nodes exist in both trees: " + overlappingNodes);
        }
    }

    private static <N, V> Set<N> getNodesWithoutRootNode(final Tree<N, V, ImmutableValueGraph<N, V>> tree) {
        return Sets.difference(tree.graph().nodes(), Set.of(tree.rootNode()));
    }

    private static <N, V> MutableValueGraph<N, V> createEmptyGraph(final TreeNode<N, V, ImmutableValueGraph<N, V>> treeNode) {
        return ValueGraphBuilder
                .from(treeNode.tree().graph())
                .build();
    }

    private static <N, V> void mergeTreeIntoTreeNode(
            final Tree<N, V, ImmutableValueGraph<N, V>> tree,
            final TreeNode<N, V, ImmutableValueGraph<N, V>> treeNode,
            final MutableValueGraph<N, V> mergedGraph) {
        addAllNodesWithoutTreeNode(tree, treeNode, mergedGraph);
        addEdgesNotConnectedToTreeNode(treeNode, mergedGraph);
        redirectIncomingEdgeOfTreeNodeToRootNodeOfTree(treeNode, tree, mergedGraph);
        attachOutgoingEdgesOfTreeNodeToRootNodeOfTree(treeNode, tree, mergedGraph);
        addEdges(tree, mergedGraph);
    }

    private static <N, V> void addAllNodesWithoutTreeNode(
            final Tree<N, V, ImmutableValueGraph<N, V>> tree,
            final TreeNode<N, V, ImmutableValueGraph<N, V>> treeNode,
            final MutableValueGraph<N, V> dst) {
        TreeMerger
                .getAllNodesWithoutTreeNode(tree, treeNode)
                .forEach(dst::addNode);
    }

    private static <N, V> Set<N> getAllNodesWithoutTreeNode(
            final Tree<N, V, ImmutableValueGraph<N, V>> tree,
            final TreeNode<N, V, ImmutableValueGraph<N, V>> treeNode) {
        return com.google.common.collect.Sets.union(
                tree.graph().nodes(),
                Sets.difference(
                        treeNode.tree().graph().nodes(),
                        Set.of(treeNode.node())));
    }

    private static <N, V> void addEdgesNotConnectedToTreeNode(
            final TreeNode<N, V, ImmutableValueGraph<N, V>> treeNode,
            final MutableValueGraph<N, V> dst) {
        TreeMerger
                .getEdgesNotConnectedToTreeNode(treeNode)
                .forEach(edgesNotConnectedToTreeNode -> Graphs.addEdge(dst, edgesNotConnectedToTreeNode));
    }

    private static <N, V> List<Edge<N, V>> getEdgesNotConnectedToTreeNode(final TreeNode<N, V, ImmutableValueGraph<N, V>> treeNode) {
        return Graphs
                .getEdges(treeNode.tree().graph())
                .stream()
                .filter(edge -> !isEdgeConnectedToNode(edge, treeNode.node()))
                .toList();
    }

    private static <N, V> boolean isEdgeConnectedToNode(final Edge<N, V> edge, final N node) {
        return Set
                .of(edge.endpointPair().source(), edge.endpointPair().target())
                .contains(node);
    }

    private static <N, V> void redirectIncomingEdgeOfTreeNodeToRootNodeOfTree(
            final TreeNode<N, V, ImmutableValueGraph<N, V>> treeNode,
            final Tree<N, V, ImmutableValueGraph<N, V>> tree,
            final MutableValueGraph<N, V> dst) {
        TreeMerger
                .redirectIncomingEdgeOfTreeNodeToTarget(treeNode, tree.rootNode())
                .ifPresent(newEdge -> Graphs.addEdge(dst, newEdge));
    }

    private static <N, V> Optional<Edge<N, V>> redirectIncomingEdgeOfTreeNodeToTarget(
            final TreeNode<N, V, ImmutableValueGraph<N, V>> treeNode,
            final N target) {
        return treeNode
                .incomingEdge()
                .map(incomingEdge -> incomingEdge.asEdgeHavingTarget(target));
    }

    private static <N, V> void attachOutgoingEdgesOfTreeNodeToRootNodeOfTree(
            final TreeNode<N, V, ImmutableValueGraph<N, V>> treeNode,
            final Tree<N, V, ImmutableValueGraph<N, V>> tree,
            final MutableValueGraph<N, V> dst) {
        TreeMerger
                .attachOutgoingEdgesOfTreeNodeToSource(treeNode, tree.rootNode())
                .forEach(newEdge -> Graphs.addEdge(dst, newEdge));
    }

    private static <N, V> List<Edge<N, V>> attachOutgoingEdgesOfTreeNodeToSource(
            final TreeNode<N, V, ImmutableValueGraph<N, V>> treeNode,
            final N source) {
        return treeNode
                .outgoingEdges()
                .stream()
                .map(outgoingEdge -> outgoingEdge.asEdgeHavingSource(source))
                .filter(edge -> !edge.isSelfLoop())
                .toList();
    }

    private static <N, V> void addEdges(final Tree<N, V, ImmutableValueGraph<N, V>> tree,
                                        final MutableValueGraph<N, V> dst) {
        Graphs
                .getEdges(tree.graph())
                .forEach(edge -> Graphs.addEdge(dst, edge));
    }
}
