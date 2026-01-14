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
        final MutableValueGraph<N, V> resultGraph = Graphs.toMutableValueGraph(subtreeToReplace.tree().graph());
        final Optional<Edge<N, V>> edgeFromParentToRootOfReplacementTree =
                subtreeToReplace
                        .tree()
                        .incomingEdgeOf(subtreeToReplace.rootNodeOfSubtree())
                        .map(incomingEdge -> createEdgeFromParentToRootOfReplacementTree(incomingEdge, replacementTree));
        removeSubtreeFromGraph(subtreeToReplace, resultGraph);
        GraphCopiers.copySrcToDst(replacementTree.graph(), resultGraph);
        connectParentToRootOfReplacementTree(resultGraph, edgeFromParentToRootOfReplacementTree);
        return new Tree<>(ImmutableValueGraph.copyOf(resultGraph));
    }

    private static <N, V> void removeSubtreeFromGraph(final Subtree<N, V, ImmutableValueGraph<N, V>> subtree,
                                                      final MutableValueGraph<N, V> graph) {
        subtree.getSubtreeNodes().forEach(graph::removeNode);
    }

    private static <N, V> void connectParentToRootOfReplacementTree(final MutableValueGraph<N, V> resultGraph,
                                                                    final Optional<Edge<N, V>> edgeFromParentToRootOfReplacementTree) {
        edgeFromParentToRootOfReplacementTree.ifPresent(
                _edgeFromParentToRootOfReplacementTree ->
                        Graphs.addEdge(resultGraph, _edgeFromParentToRootOfReplacementTree));
    }

    private static <N, V> Edge<N, V> createEdgeFromParentToRootOfReplacementTree(
            final Edge<N, V> incomingEdge,
            final Tree<N, V, ImmutableValueGraph<N, V>> replacementTree) {
        final N parent = incomingEdge.endpointPair().source();
        return new Edge<>(
                EndpointPair.ordered(
                        parent,
                        replacementTree.rootNode()),
                incomingEdge.value());
    }
}
