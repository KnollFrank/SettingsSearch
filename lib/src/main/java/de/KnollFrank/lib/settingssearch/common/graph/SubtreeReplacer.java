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
                .withinTree(subtreeToReplace.tree())
                .replace(subtreeToReplace.asTree(), replacementTree);
    }

    private static class ReplacerWorker<N, V> {

        private final Tree<N, V, ImmutableValueGraph<N, V>> tree;
        private final MutableValueGraph<N, V> resultGraph;

        public static <N, V> ReplacerWorker<N, V> withinTree(final Tree<N, V, ImmutableValueGraph<N, V>> tree) {
            return new ReplacerWorker<>(tree);
        }

        public ReplacerWorker(final Tree<N, V, ImmutableValueGraph<N, V>> tree) {
            this.tree = tree;
            this.resultGraph = Graphs.toMutableValueGraph(tree.graph());
        }

        public Tree<N, V, ImmutableValueGraph<N, V>> replace(
                final Tree<N, V, ImmutableValueGraph<N, V>> treeToReplace,
                final Tree<N, V, ImmutableValueGraph<N, V>> replacementTree) {
            remove(treeToReplace);
            {
                add(replacementTree);
                final var edgeToRootOfReplacementTree = createEdgeFromParentOfNodeToTarget(treeToReplace.rootNode(), replacementTree.rootNode());
                add(edgeToRootOfReplacementTree);
            }
            return new Tree<>(ImmutableValueGraph.copyOf(resultGraph));
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
        private void remove(final Tree<N, V, ImmutableValueGraph<N, V>> tree) {
            tree.graph().nodes().forEach(resultGraph::removeNode);
        }

        private void add(final Tree<N, V, ImmutableValueGraph<N, V>> tree) {
            GraphCopiers.copySrcToDst(tree.graph(), resultGraph);
        }

        private Optional<Edge<N, V>> createEdgeFromParentOfNodeToTarget(final N node, final N target) {
            return tree
                    .incomingEdgeOf(node)
                    .map(edgeToNode -> {
                        final N parentOfNode = edgeToNode.endpointPair().source();
                        return new Edge<>(
                                EndpointPair.ordered(parentOfNode, target),
                                edgeToNode.value());
                    });
        }

        private void add(final Optional<Edge<N, V>> edge) {
            edge.ifPresent(_edge -> Graphs.addEdge(resultGraph, _edge));
        }
    }
}
