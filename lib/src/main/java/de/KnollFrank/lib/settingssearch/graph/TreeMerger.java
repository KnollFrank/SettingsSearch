package de.KnollFrank.lib.settingssearch.graph;

import com.google.common.collect.Sets;
import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;

import java.util.Set;
import java.util.stream.Collectors;
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

        // Pre-validation: Check for overlapping nodes, which would lead to an invalid tree.
        // The children of the tree to be merged must not exist in the target tree.
        final Set<N> childrenOfTreeToMerge =
                tree.graph().nodes()
                        .stream()
                        .filter(node -> !node.equals(tree.rootNode()))
                        .collect(Collectors.toSet());

        final Set<N> nodesOfTargetTree = treeNode.tree().graph().nodes();
        final Sets.SetView<N> intersection = Sets.intersection(childrenOfTreeToMerge, nodesOfTargetTree);
        if (!intersection.isEmpty()) {
            throw new IllegalArgumentException(
                    "Merge would result in an invalid tree. The following nodes exist in both trees: " + intersection);
        }

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
        treeNode.tree().outgoingEdgesOf(treeNode.node()).forEach(outgoingEdge ->
                                                                         mergedGraph.putEdgeValue(
                                                                                 tree.rootNode(),
                                                                                 outgoingEdge.endpointPair().target(),
                                                                                 outgoingEdge.value()));

        // 5. Add all edges from the new tree to be merged.
        tree.graph().edges().forEach(edge ->
                                             mergedGraph.putEdgeValue(
                                                     edge.source(),
                                                     edge.target(),
                                                     Graphs.getEdgeValue(edge, tree.graph())));

        // The constructor call will validate the final merged graph.
        return new Tree<>(ImmutableValueGraph.copyOf(mergedGraph));
    }
}

