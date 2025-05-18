package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import org.jgrapht.Graph;
import org.jgrapht.traverse.BreadthFirstIterator;

import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public class SubtreeReplacer {

    @FunctionalInterface
    public interface EdgeFactory<V, E> {
        /**
         * Creates a new edge based on an original edge.
         *
         * @param source       The source vertex for the new edge.
         * @param target       The target vertex for the new edge.
         * @param originalEdge The original edge that this new edge is based on
         *                     (e.g., for copying properties or labels).
         *                     This will be the edge that previously connected to the
         *                     {@code nodeToReplace} if replacing a child, or an edge from
         *                     the replacementTree, or an edge from the originalGraph
         *                     being copied.
         * @return The new edge.
         */
        E createEdge(V source, V target, E originalEdge);
    }

    private record ParentAndEdge<V, E>(V parent, E edgeToChild) {
    }

    public static <V, E> Graph<V, E> replaceSubtreeWithTree(final Graph<V, E> originalGraph,
                                                            final V nodeToReplace,
                                                            final Graph<V, E> replacementTree,
                                                            final Supplier<Graph<V, E>> graphSupplier,
                                                            final EdgeFactory<V, E> edgeFactory) {
        if (!originalGraph.containsVertex(nodeToReplace)) {
            return originalGraph;
        }

        final Graph<V, E> resultGraph = graphSupplier.get();
        final Optional<ParentAndEdge<V, E>> parentInfoOpt = getParentAndIncomingEdge(originalGraph, nodeToReplace);
        copyPartsOfGraph(
                originalGraph,
                getSubtreeVertices(originalGraph, nodeToReplace),
                resultGraph,
                edgeFactory);

        // Integrate the replacement tree (if it's not empty)
        final Optional<V> replacementRootOpt = GraphUtils.getRootNode(replacementTree);

        replacementRootOpt.ifPresent(replacementRoot -> {
            // Add all vertices and edges from the replacement tree
            replacementTree.vertexSet().forEach(resultGraph::addVertex);
            for (final E e : replacementTree.edgeSet()) {
                final V source = replacementTree.getEdgeSource(e);
                final V target = replacementTree.getEdgeTarget(e);
                resultGraph.addEdge(source, target, edgeFactory.createEdge(source, target, e));
            }

            // Connect the parent (if any) to the root of the replacement tree
            parentInfoOpt.ifPresent(parentInfo -> {
                if (resultGraph.containsVertex(parentInfo.parent)) {
                    final E connectingEdge =
                            edgeFactory.createEdge(
                                    parentInfo.parent,
                                    replacementRoot,
                                    parentInfo.edgeToChild); // Pass the original incoming edge directly
                    resultGraph.addEdge(parentInfo.parent, replacementRoot, connectingEdge);
                }
            });
        });
        return resultGraph;
    }

    private static <V, E> void copyPartsOfGraph(final Graph<V, E> originalGraph,
                                                final Set<V> subtreeVerticesToRemove,
                                                final Graph<V, E> resultGraph,
                                                final EdgeFactory<V, E> edgeFactory) {
        copyNodes(originalGraph, subtreeVerticesToRemove, resultGraph);
        copyEdges(originalGraph, edgeFactory, subtreeVerticesToRemove, resultGraph);
    }

    private static <V, E> void copyNodes(final Graph<V, E> originalGraph,
                                         final Set<V> nodesToRemove,
                                         final Graph<V, E> resultGraph) {
        Sets
                .difference(originalGraph.vertexSet(), nodesToRemove)
                .forEach(resultGraph::addVertex);
    }

    private static <V, E> void copyEdges(final Graph<V, E> originalGraph,
                                         final EdgeFactory<V, E> edgeFactory,
                                         final Set<V> nodesToRemove,
                                         final Graph<V, E> resultGraph) {
        for (final E edge : originalGraph.edgeSet()) {
            final V source = originalGraph.getEdgeSource(edge);
            final V target = originalGraph.getEdgeTarget(edge);
            if (!nodesToRemove.contains(source) && !nodesToRemove.contains(target)) {
                resultGraph.addEdge(source, target, edgeFactory.createEdge(source, target, edge));
            }
        }
    }

    private static <V, E> Optional<ParentAndEdge<V, E>> getParentAndIncomingEdge(final Graph<V, E> graph,
                                                                                 final V node) {
        final Set<E> incomingEdges = graph.incomingEdgesOf(node);
        if (!incomingEdges.isEmpty()) {
            final E edgeToChild = incomingEdges.iterator().next();
            return Optional.of(
                    new ParentAndEdge<>(
                            graph.getEdgeSource(edgeToChild),
                            edgeToChild));
        }
        return Optional.empty();
    }

    private static <V, E> Set<V> getSubtreeVertices(final Graph<V, E> graph, final V startNode) {
        return graph.containsVertex(startNode) ?
                ImmutableSet.copyOf(new BreadthFirstIterator<>(graph, startNode)) :
                Set.of();
    }
}