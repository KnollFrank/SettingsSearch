package de.KnollFrank.lib.settingssearch.graph;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;

import java.util.List;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.common.Sets;
import de.KnollFrank.lib.settingssearch.common.graph.Edge;
import de.KnollFrank.lib.settingssearch.common.graph.Graphs;
import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.common.graph.TreeNode;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
// FK-TODO: refactor
public class TreeMerger {

    public static <N, V> Tree<N, V, ImmutableValueGraph<N, V>> mergeTreeIntoTreeNode(
            final Tree<N, V, ImmutableValueGraph<N, V>> tree,
            final TreeNode<N, V, ImmutableValueGraph<N, V>> treeNode) {
        // FK-TODO: es fehlt ein Test dafür, dass die rootNode von tree mit der treeNode übereinstimmen muss bzgl. equals()
        assertNodesDoNotOverlap(tree, treeNode);
        final MutableValueGraph<N, V> mergedGraph =
                ValueGraphBuilder
                        .from(treeNode.tree().graph())
                        .build();
        addNodes(tree, treeNode, mergedGraph);
        addEdges(treeNode, mergedGraph);
        redirectIncomingEdgeOfTreeNodeToRootNodeOfTree(treeNode, tree, mergedGraph);
        attachOutgoingEdgesOfTreeNodeToRootNodeOfTree(treeNode, tree, mergedGraph);

        // 5. Add all edges from the new tree to be merged.
        tree.graph().edges().forEach(edge ->
                                             mergedGraph.putEdgeValue(
                                                     edge.source(),
                                                     edge.target(),
                                                     Graphs.getEdgeValue(edge, tree.graph())));

        // The constructor call will validate the final merged graph.
        return new Tree<>(ImmutableValueGraph.copyOf(mergedGraph));
    }

    private static <N, V> void assertNodesDoNotOverlap(final Tree<N, V, ImmutableValueGraph<N, V>> tree,
                                                       final TreeNode<N, V, ImmutableValueGraph<N, V>> treeNode) {
        final Set<N> overlappingNodes =
                Sets.intersection(
                        getChildrenOfTreeToMerge(tree),
                        treeNode.tree().graph().nodes());
        if (!overlappingNodes.isEmpty()) {
            throw new IllegalArgumentException(
                    "Merge would result in an invalid tree. The following nodes exist in both trees: " + overlappingNodes);
        }
    }

    private static <N, V> Set<N> getChildrenOfTreeToMerge(final Tree<N, V, ImmutableValueGraph<N, V>> tree) {
        return Sets.difference(tree.graph().nodes(), Set.of(tree.rootNode()));
    }

    private static <N, V> void addNodes(final Tree<N, V, ImmutableValueGraph<N, V>> tree, final TreeNode<N, V, ImmutableValueGraph<N, V>> treeNode, final MutableValueGraph<N, V> mergedGraph) {
        TreeMerger
                .getNodesToAdd(tree, treeNode)
                .forEach(mergedGraph::addNode);
    }

    private static <N, V> Set<N> getNodesToAdd(final Tree<N, V, ImmutableValueGraph<N, V>> tree,
                                               final TreeNode<N, V, ImmutableValueGraph<N, V>> treeNode) {
        return com.google.common.collect.Sets.union(
                Sets.difference(
                        treeNode.tree().graph().nodes(),
                        Set.of(treeNode.node())),
                tree.graph().nodes());
    }

    // 2. Add all edges from the original tree that are not connected to the "mergePoint".
    private static <N, V> void addEdges(final TreeNode<N, V, ImmutableValueGraph<N, V>> treeNode,
                                        final MutableValueGraph<N, V> mergedGraph) {
        Graphs
                .getEdges(treeNode.tree().graph())
                .stream()
                .filter(edge -> !isEdgeConnectedToNode(edge, treeNode.node()))
                .forEach(edge -> Graphs.addEdge(mergedGraph, edge));
    }

    private static <N, V> boolean isEdgeConnectedToNode(final Edge<N, V> edge, final N node) {
        return Set
                .of(edge.endpointPair().source(), edge.endpointPair().target())
                .contains(node);
    }

    private static <N, V> void redirectIncomingEdgeOfTreeNodeToRootNodeOfTree(
            final TreeNode<N, V, ImmutableValueGraph<N, V>> treeNode,
            final Tree<N, V, ImmutableValueGraph<N, V>> tree,
            final MutableValueGraph<N, V> mergedGraph) {
        treeNode
                .incomingEdge()
                .ifPresent(incomingEdgeOfTreeNode ->
                                   Graphs.addEdge(
                                           mergedGraph,
                                           new Edge<>(
                                                   EndpointPair.ordered(
                                                           incomingEdgeOfTreeNode.endpointPair().source(),
                                                           tree.rootNode()),
                                                   incomingEdgeOfTreeNode.value())));
    }

    private static <N, V> void attachOutgoingEdgesOfTreeNodeToRootNodeOfTree(
            final TreeNode<N, V, ImmutableValueGraph<N, V>> treeNode,
            final Tree<N, V, ImmutableValueGraph<N, V>> tree,
            final MutableValueGraph<N, V> mergedGraph) {
        TreeMerger
                .attachOutgoingEdgesOfTreeNodeToSource(tree.rootNode(), treeNode)
                .forEach(newEdge -> Graphs.addEdge(mergedGraph, newEdge));
    }

    private static <N, V> List<Edge<N, V>> attachOutgoingEdgesOfTreeNodeToSource(final N source, final TreeNode<N, V, ImmutableValueGraph<N, V>> treeNode) {
        return treeNode
                .outgoingEdges()
                .stream()
                .map(outgoingEdge -> getEdgeHavingSource(outgoingEdge, source))
                .filter(edge -> !edge.isSelfLoop())
                .toList();
    }

    private static <N, V> Edge<N, V> getEdgeHavingSource(final Edge<N, V> edge, final N source) {
        return new Edge<>(
                EndpointPair.ordered(
                        source,
                        edge.endpointPair().target()),
                edge.value());
    }
}
