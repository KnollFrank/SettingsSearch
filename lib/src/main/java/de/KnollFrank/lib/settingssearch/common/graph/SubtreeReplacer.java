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
        return ReplacerWorker
                .withinGraph(subtreeToReplace.tree().graph())
                .replaceSubtreeWithTree(subtreeToReplace, replacementTree);
    }

    private static class ReplacerWorker<N, V> {

        private final MutableValueGraph<N, V> resultGraph;

        public static <N, V> ReplacerWorker<N, V> withinGraph(final ImmutableValueGraph<N, V> graph) {
            return new ReplacerWorker<>(graph);
        }

        public ReplacerWorker(final ImmutableValueGraph<N, V> graph) {
            this.resultGraph = Graphs.toMutableValueGraph(graph);
        }

        public Tree<N, V, ImmutableValueGraph<N, V>> replaceSubtreeWithTree(
                final Subtree<N, V, ImmutableValueGraph<N, V>> subtreeToReplace,
                final Tree<N, V, ImmutableValueGraph<N, V>> replacementTree) {
            remove(subtreeToReplace);
            {
                add(replacementTree);
                final var edgeToRootOfReplacementTree = createEdgeFromParentOfTreeAtNodeToTarget(subtreeToReplace.asTreeAtNode(), replacementTree.rootNode());
                add(edgeToRootOfReplacementTree);
            }
            return new Tree<>(ImmutableValueGraph.copyOf(resultGraph));
        }

        private static <N, V> Optional<Edge<N, V>> createEdgeFromParentOfTreeAtNodeToTarget(
                final TreeAtNode<N, V, ImmutableValueGraph<N, V>> treeAtNode,
                final N target) {
            return ReplacerWorker
                    .getIncomingEdgeOf(treeAtNode)
                    .map(edgeToTreeAtNode -> {
                        final N parentOfTreeAtNode = edgeToTreeAtNode.endpointPair().source();
                        return new Edge<>(
                                EndpointPair.ordered(
                                        parentOfTreeAtNode,
                                        target),
                                edgeToTreeAtNode.value());
                    });
        }

        private static <N, V> Optional<Edge<N, V>> getIncomingEdgeOf(final TreeAtNode<N, V, ImmutableValueGraph<N, V>> treeAtNode) {
            return treeAtNode.tree().incomingEdgeOf(treeAtNode.nodeOfTree());
        }

        /**
         * Removes all nodes of the given subtree from the graph.
         * Guava automatically removes all incident edges.
         * <pre>
         * Example:
         *
         * BEFORE (resultGraph):      subtree to remove:
         *      P                         >R<
         *     / \                         |
         *    Q   R                        A
         *        |
         *        A
         *
         * AFTER (resultGraph):
         *      P
         *     /
         *    Q
         * </pre>
         */
        private void remove(final Subtree<N, V, ImmutableValueGraph<N, V>> subtree) {
            subtree.getSubtreeNodes().forEach(resultGraph::removeNode);
        }

        private void add(final Tree<N, V, ImmutableValueGraph<N, V>> tree) {
            GraphCopiers.copySrcToDst(tree.graph(), resultGraph);
        }

        private void add(final Optional<Edge<N, V>> edge) {
            edge.ifPresent(_edge -> Graphs.addEdge(resultGraph, _edge));
        }
    }
}
