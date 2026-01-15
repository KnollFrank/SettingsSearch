package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.MutableValueGraph;

import java.util.Optional;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class SubtreeReplacer {

    public static <N, V> Tree<N, V, ImmutableValueGraph<N, V>> replaceSubtreeWithTree(
            final Subtree<N, V, ImmutableValueGraph<N, V>> subtreeToReplace,
            final Tree<N, V, ImmutableValueGraph<N, V>> replacementTree) {
        final var resultGraph = Graphs.toMutableValueGraph(subtreeToReplace.tree().graph());
        final var edgeToRootOfReplacementTree = createEdgeFromParentOfTreeAtNodeToTarget(subtreeToReplace.asTreeAtNode(), replacementTree.rootNode());
        removeSubtreeFromGraph(subtreeToReplace, resultGraph);
        addTreeAndEdgeToGraph(replacementTree, edgeToRootOfReplacementTree, resultGraph);
        return new Tree<>(ImmutableValueGraph.copyOf(resultGraph));
    }

    private static <N, V> Optional<Edge<N, V>> createEdgeFromParentOfTreeAtNodeToTarget(
            final TreeAtNode<N, V, ImmutableValueGraph<N, V>> treeAtNode,
            final N target) {
        return SubtreeReplacer
                .getIncomingEdgeOf(treeAtNode)
                .map(incomingEdge ->
                             new Edge<>(
                                     EndpointPair.ordered(
                                             incomingEdge.endpointPair().source(),
                                             target),
                                     incomingEdge.value()));
    }

    private static <N, V> Optional<Edge<N, V>> getIncomingEdgeOf(final TreeAtNode<N, V, ImmutableValueGraph<N, V>> treeAtNode) {
        return treeAtNode.tree().incomingEdgeOf(treeAtNode.nodeOfTree());
    }

    private static <N, V> void removeSubtreeFromGraph(final Subtree<N, V, ImmutableValueGraph<N, V>> subtree,
                                                      final MutableValueGraph<N, V> graph) {
        subtree.getSubtreeNodes().forEach(graph::removeNode);
    }

    private static <N, V> void addTreeAndEdgeToGraph(final Tree<N, V, ImmutableValueGraph<N, V>> tree,
                                                     final Optional<Edge<N, V>> edge,
                                                     final MutableValueGraph<N, V> graph) {
        GraphCopiers.copySrcToDst(tree.graph(), graph);
        addEdge(edge, graph);
    }

    private static <N, V> void addEdge(final Optional<Edge<N, V>> edge,
                                       final MutableValueGraph<N, V> graph) {
        edge.ifPresent(_edge -> Graphs.addEdge(graph, _edge));
    }
}
