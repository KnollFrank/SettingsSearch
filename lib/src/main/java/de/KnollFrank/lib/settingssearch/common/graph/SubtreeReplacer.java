package de.KnollFrank.lib.settingssearch.common.graph;

import org.jgrapht.Graph;
import org.jgrapht.traverse.BreadthFirstIterator;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public class SubtreeReplacer {

    @FunctionalInterface
    public interface EdgeFactory<V, E> {

        // FK-TODO: make originalEdge Optional<E>
        E createEdge(V source, V target, E originalEdge);
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

        // 1. Identify parent of nodeToReplace and the edge connecting them (if any)
        V parentOfNodeToReplace = null;
        E edgeToNodeToReplace = null;
        final Set<E> incomingEdges = originalGraph.incomingEdgesOf(nodeToReplace);
        if (!incomingEdges.isEmpty()) {
            edgeToNodeToReplace = incomingEdges.iterator().next();
            parentOfNodeToReplace = originalGraph.getEdgeSource(edgeToNodeToReplace);
        }

        // 2. Identify the subtree to be removed (all descendants of nodeToReplace, including itself)
        final Set<V> subtreeVerticesToRemove = getSubtreeVertices(originalGraph, nodeToReplace);

        // 3. Copy parts of the original graph that are NOT part of the subtree being replaced
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

        // 4. Integrate the replacement tree (if it's not empty)
        final Optional<V> replacementRootOptional = getRootOfTree(replacementTree);

        if (replacementRootOptional.isPresent()) {
            final V replacementRoot = replacementRootOptional.get();

            // Add all vertices and edges from the replacement tree
            // It's generally safer to iterate and add rather than Graphs.addGraphNatively
            // if the vertex/edge instances need to be distinct or processed by the factory.
            replacementTree.vertexSet().forEach(resultGraph::addVertex);
            for (final E e : replacementTree.edgeSet()) {
                final V source = replacementTree.getEdgeSource(e);
                final V target = replacementTree.getEdgeTarget(e);
                resultGraph.addEdge(source, target, edgeFactory.createEdge(source, target, e));
            }

            if (parentOfNodeToReplace != null) {
                if (resultGraph.containsVertex(parentOfNodeToReplace)) { // Should always be true if copied correctly
                    final E connectingEdge = edgeFactory.createEdge(parentOfNodeToReplace, replacementRoot, edgeToNodeToReplace);
                    resultGraph.addEdge(parentOfNodeToReplace, replacementRoot, connectingEdge);
                }
            }
        }
        return resultGraph;
    }

    /**
     * Helper method to get all vertices in the subtree rooted at a given startNode.
     * Uses JGraphT's BreadthFirstIterator for traversal.
     */
    private static <V, E> Set<V> getSubtreeVertices(final Graph<V, E> graph, final V startNode) {
        final Set<V> visited = new HashSet<>();
        if (!graph.containsVertex(startNode)) {
            return visited; // Return empty set if startNode is not in the graph
        }

        // Create a BreadthFirstIterator starting from startNode
        final BreadthFirstIterator<V, E> bfsIterator = new BreadthFirstIterator<>(graph, startNode);

        // Iterate and add all reachable vertices to the set
        while (bfsIterator.hasNext()) {
            visited.add(bfsIterator.next());
        }
        return visited;
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