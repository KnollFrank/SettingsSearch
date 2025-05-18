package de.KnollFrank.lib.settingssearch.common.graph;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.function.Supplier;

public class SubtreeReplacer {

    @FunctionalInterface
    public interface EdgeFactory<V, E> {
        E createEdge(V source, V target, E originalEdge);
    }

    /**
     * Replaces a subtree rooted at {@code nodeToReplace} in the {@code originalGraph}
     * with the {@code replacementTree}.
     * <p>
     * This operation is non-mutating with respect to the {@code originalGraph} and
     * {@code replacementTree}. It returns a new graph instance.
     * <p>
     * If {@code nodeToReplace} is not found in {@code originalGraph}, the original graph
     * instance is returned.
     * <p>
     * If {@code replacementTree} is empty, the subtree rooted at {@code nodeToReplace}
     * is effectively pruned.
     * <p>
     * Both {@code originalGraph} and {@code replacementTree} are assumed to be
     * single-rooted trees (or directed acyclic graphs where {@code nodeToReplace}
     * and the root of {@code replacementTree} are clearly identifiable).
     *
     * @param originalGraph   The graph containing the subtree to be replaced. Assumed to be a tree.
     * @param nodeToReplace   The root of the subtree to be replaced.
     * @param replacementTree The tree that will replace the subtree. Assumed to be a single tree.
     *                        If empty, the subtree is removed.
     * @param graphSupplier   A supplier for creating new graph instances.
     * @param edgeFactory     A factory for creating new edges, particularly the edge
     *                        connecting the parent of the replaced subtree to the root of
     *                        the replacement tree. The third parameter to the factory
     *                        (originalEdge) will be the original edge that connected
     *                        the parent to {@code nodeToReplace}, or null if
     *                        {@code nodeToReplace} was the root of {@code originalGraph}.
     * @param <V>             The vertex type.
     * @param <E>             The edge type.
     * @return A new graph with the subtree replaced, or the original graph instance
     * if {@code nodeToReplace} is not found.
     */
    public static <V, E> Graph<V, E> replaceSubtreeWithTree(
            final Graph<V, E> originalGraph,
            final V nodeToReplace,
            final Graph<V, E> replacementTree,
            final Supplier<Graph<V, E>> graphSupplier,
            final EdgeFactory<V, E> edgeFactory) {

        if (!originalGraph.containsVertex(nodeToReplace)) {
            return originalGraph; // Node not found, return original instance
        }

        final Graph<V, E> resultGraph = graphSupplier.get();

        // 1. Identify parent of nodeToReplace and the edge connecting them (if any)
        V parentOfNodeToReplace = null;
        E edgeToNodeToReplace = null;
        final Set<E> incomingEdges = originalGraph.incomingEdgesOf(nodeToReplace);
        if (!incomingEdges.isEmpty()) {
            // Assuming a tree structure, there's at most one incoming edge to a non-root node
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
            // Add edge only if both source and target are outside the removed subtree
            if (!subtreeVerticesToRemove.contains(source) && !subtreeVerticesToRemove.contains(target)) {
                // Create a new edge instance for the new graph, possibly with the same label/attributes
                // For simplicity, we use the edge factory here as well, though it could be a direct copy.
                // The critical use of edgeFactory is for the connecting edge.
                resultGraph.addEdge(source, target, edgeFactory.createEdge(source, target, e));
            }
        }

        // 4. Integrate the replacement tree (if it's not empty)
        final Optional<V> replacementRootOptional = getRootOfTree(replacementTree);

        if (replacementRootOptional.isPresent()) {
            final V replacementRoot = replacementRootOptional.get();

            // Add all vertices and edges from the replacement tree
            for (final V v : replacementTree.vertexSet()) {
                resultGraph.addVertex(v); // AddVertex handles duplicates gracefully
            }
            for (final E e : replacementTree.edgeSet()) {
                final V source = replacementTree.getEdgeSource(e);
                final V target = replacementTree.getEdgeTarget(e);
                // Create new edge instances for the result graph using their original labels/properties.
                resultGraph.addEdge(source, target, edgeFactory.createEdge(source, target, e));
            }

            // 5. Connect the parent (if any) to the root of the replacement tree
            if (parentOfNodeToReplace != null) {
                // If parentOfNodeToReplace was part of the original graph and not removed,
                // and replacementRoot is present, connect them.
                if (resultGraph.containsVertex(parentOfNodeToReplace)) {
                    // Use the edgeFactory, passing the original edge that connected
                    // parentOfNodeToReplace to nodeToReplace.
                    final E connectingEdge = edgeFactory.createEdge(parentOfNodeToReplace, replacementRoot, edgeToNodeToReplace);
                    resultGraph.addEdge(parentOfNodeToReplace, replacementRoot, connectingEdge);
                }
            }
            // If nodeToReplace was the root of the original graph, the replacementRoot
            // becomes the new root of the resultGraph (already handled by adding its vertices/edges).
        }
        // If replacementTree is empty, the subtree is simply removed, and no new connections are made.
        // If replacementRootOptional is empty (e.g. replacement tree has no nodes), nothing to add or connect.

        return resultGraph;
    }

    /**
     * Helper method to get all vertices in the subtree rooted at a given startNode.
     * Performs a breadth-first or depth-first traversal.
     */
    private static <V, E> Set<V> getSubtreeVertices(final Graph<V, E> graph, final V startNode) {
        final Set<V> visited = new HashSet<>();
        final Queue<V> queue = new LinkedList<>();

        if (graph.containsVertex(startNode)) {
            queue.add(startNode);
            visited.add(startNode);
        }

        while (!queue.isEmpty()) {
            final V current = queue.poll();
            for (final V successor : Graphs.successorListOf(graph, current)) {
                if (visited.add(successor)) { // If successfully added (i.e., not visited before)
                    queue.add(successor);
                }
            }
        }
        return visited;
    }

    /**
     * Helper method to find the root of a tree.
     * Assumes a single-rooted tree (a node with an in-degree of 0).
     * If multiple such nodes exist or none, behavior might be unpredictable based on graph type.
     * For a well-formed tree, this should return its unique root.
     *
     * @return Optional containing the root, or empty if the graph is empty or not a clear tree.
     */
    private static <V, E> Optional<V> getRootOfTree(final Graph<V, E> tree) {
        if (tree.vertexSet().isEmpty()) {
            return Optional.empty();
        }
        return tree.vertexSet().stream()
                .filter(v -> tree.inDegreeOf(v) == 0)
                .findFirst(); // In a single-rooted tree, there should be only one.
    }
}