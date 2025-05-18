package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import org.jgrapht.Graph;
import org.jgrapht.traverse.BreadthFirstIterator;

import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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
        // FK-TODO: remove source and target? Then rename createEdge(E originalEdge) to clone(E edge)
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
        GraphUtils
                .getRootNode(replacementTree)
                .ifPresent(
                        replacementRoot -> {
                            copyGraphFromSrc2Dst(replacementTree, resultGraph, edgeFactory);
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

    private static <V, E> void copyGraphFromSrc2Dst(final Graph<V, E> src,
                                                    final Graph<V, E> dst,
                                                    final EdgeFactory<V, E> edgeFactory) {
        addNodesToGraph(src.vertexSet(), dst);
        copyEdgesFromSrc2Dst(src, src.edgeSet(), dst, edgeFactory);
    }

    private static <V, E> void copyPartsOfGraph(final Graph<V, E> originalGraph,
                                                final Set<V> subtreeVerticesToRemove,
                                                final Graph<V, E> resultGraph,
                                                final EdgeFactory<V, E> edgeFactory) {
        addNodesToGraph(
                Sets.difference(originalGraph.vertexSet(), subtreeVerticesToRemove),
                resultGraph);
        copyEdgesFromSrc2Dst(
                originalGraph,
                getEdgesToRetain(originalGraph, subtreeVerticesToRemove),
                resultGraph,
                edgeFactory);
    }

    private static <V, E> void addNodesToGraph(final Set<V> nodes, final Graph<V, E> graph) {
        nodes.forEach(graph::addVertex);
    }

    private static <V, E> void copyEdgesFromSrc2Dst(final Graph<V, E> src,
                                                    final Set<E> edgesOfSrcToCopy,
                                                    final Graph<V, E> dst,
                                                    final EdgeFactory<V, E> edgeFactory) {
        for (final E edge : edgesOfSrcToCopy) {
            final V source = src.getEdgeSource(edge);
            final V target = src.getEdgeTarget(edge);
            dst.addEdge(source, target, edgeFactory.createEdge(source, target, edge));
        }
    }

    private static <V, E> Set<E> getEdgesToRetain(final Graph<V, E> graph,
                                                  final Set<V> nodesToRemove) {
        return graph
                .edgeSet()
                .stream()
                .filter(edge -> !nodesToRemove.contains(graph.getEdgeSource(edge)) && !nodesToRemove.contains(graph.getEdgeTarget(edge)))
                .collect(Collectors.toSet());
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