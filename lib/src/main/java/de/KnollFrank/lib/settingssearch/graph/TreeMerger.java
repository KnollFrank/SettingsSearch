package de.KnollFrank.lib.settingssearch.graph;

import com.google.common.collect.Sets;
import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;

import java.util.Set;
import java.util.stream.Stream;

import de.KnollFrank.lib.settingssearch.common.graph.Graphs;
import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.common.graph.TreeNode;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
// FK-TODO: refactor
public class TreeMerger {

    public static <N, V> Tree<N, V, ImmutableValueGraph<N, V>> mergeTreeIntoTreeNode(
            final Tree<N, V, ImmutableValueGraph<N, V>> tree,
            final TreeNode<N, V, ImmutableValueGraph<N, V>> treeNode) {
        assertNodesDoNotOverlap(tree, treeNode);
        final MutableValueGraph<N, V> mergedGraph =
                ValueGraphBuilder
                        .from(treeNode.tree().graph())
                        .build();

        // 1. Add all nodes. Duplicates (only the root) are implicitly handled.
        Stream
                .concat(
                        treeNode
                                .tree()
                                .graph()
                                .nodes()
                                .stream()
                                .filter(node -> !node.equals(treeNode.node())),
                        tree
                                .graph()
                                .nodes()
                                .stream())
                .forEach(mergedGraph::addNode);

        // 2. Add all edges from the original tree that are not connected to the "mergePoint".
        treeNode.tree().graph().edges().forEach(edge -> {
            if (!edge.source().equals(treeNode.node()) && !edge.target().equals(treeNode.node())) {
                mergedGraph.putEdgeValue(
                        edge.source(),
                        edge.target(),
                        Graphs.getEdgeValue(edge, treeNode.tree().graph()));
            }
        });

        // 3. Redirect incoming edge of "mergePoint" to the root of the new tree.
        treeNode.tree().incomingEdgeOf(treeNode.node()).ifPresent(incomingEdge ->
                                                                          mergedGraph.putEdgeValue(
                                                                                  incomingEdge.endpointPair().source(),
                                                                                  tree.rootNode(),
                                                                                  incomingEdge.value()));

        // 4. Attach outgoing edges of "mergePoint" to the root of the new tree.
        treeNode.tree().outgoingEdgesOf(treeNode.node()).forEach(outgoingEdge -> {
            final N newSource = tree.rootNode();
            final N newTarget = outgoingEdge.endpointPair().target();
            // Prevent self-loops
            if (!newSource.equals(newTarget)) {
                mergedGraph.putEdgeValue(
                        newSource,
                        newTarget,
                        outgoingEdge.value());
            }
        });

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
        final Set<N> overlappingNodes = Sets.intersection(getChildrenOfTreeToMerge(tree), treeNode.tree().graph().nodes());
        if (!overlappingNodes.isEmpty()) {
            throw new IllegalArgumentException(
                    "Merge would result in an invalid tree. The following nodes exist in both trees: " + overlappingNodes);
        }
    }

    private static <N, V> Set<N> getChildrenOfTreeToMerge(final Tree<N, V, ImmutableValueGraph<N, V>> tree) {
        return Sets.difference(tree.graph().nodes(), Set.of(tree.rootNode()));
    }
}
