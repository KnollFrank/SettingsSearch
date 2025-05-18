package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.collect.ImmutableSet;

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

    public static <V, E> Graph<V, E> replaceSubtreeWithTree(
            final Graph<V, E> originalGraph,
            final V nodeToReplace,
            final Graph<V, E> replacementTree,
            final Supplier<Graph<V, E>> graphSupplier,
            final EdgeFactory<V, E> edgeFactory) {
        if (!originalGraph.containsVertex(nodeToReplace)) {
            return originalGraph;
        }

        final Graph<V, E> resultGraph = graphSupplier.get();
        final Optional<ParentAndEdge<V, E>> parentInfoOpt = getParentAndIncomingEdge(originalGraph, nodeToReplace);
        final Set<V> subtreeVerticesToRemove = getSubtreeVertices(originalGraph, nodeToReplace);

        // Copy parts of the original graph that are NOT part of the subtree being replaced
        for (final V v : originalGraph.vertexSet()) {
            if (!subtreeVerticesToRemove.contains(v)) {
                resultGraph.addVertex(v);
            }
        }
        for (final E e : originalGraph.edgeSet()) {
            final V source = originalGraph.getEdgeSource(e);
            final V target = originalGraph.getEdgeTarget(e);
            if (!subtreeVerticesToRemove.contains(source) && !subtreeVerticesToRemove.contains(target)) {
                resultGraph.addEdge(source, target, edgeFactory.createEdge(source, target, e));
            }
        }

        // Integrate the replacement tree (if it's not empty)
        final Optional<V> replacementRootOpt = getRootOfTree(replacementTree);

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

    private static <V, E> Optional<V> getRootOfTree(final Graph<V, E> tree) {
        if (tree.vertexSet().isEmpty()) {
            return Optional.empty();
        }
        return tree.vertexSet().stream()
                .filter(v -> tree.inDegreeOf(v) == 0)
                .findFirst();
    }
}