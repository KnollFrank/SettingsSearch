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
        // FK-TODO: remove source and target? Then rename createEdge(E originalEdge) to clone(E edge)
        E createEdge(V source, V target, E originalEdge);
    }

    public static <V, E> Graph<V, E> replaceSubtreeWithTree(final Graph<V, E> originalGraph,
                                                            final V nodeToReplace,
                                                            final Graph<V, E> replacementTree,
                                                            final Supplier<Graph<V, E>> emptyGraphSupplier,
                                                            final EdgeFactory<V, E> edgeFactory) {
        if (!originalGraph.containsVertex(nodeToReplace)) {
            return originalGraph;
        }
        final Graph<V, E> resultGraph = emptyGraphSupplier.get();
        copyPartsOfGraph(
                originalGraph,
                getSubtreeVertices(originalGraph, nodeToReplace),
                resultGraph,
                edgeFactory);
        integrateReplacementTreeIntoResultGraph(originalGraph, nodeToReplace, replacementTree, edgeFactory, resultGraph);
        return resultGraph;
    }

    private static <V, E> void integrateReplacementTreeIntoResultGraph(final Graph<V, E> originalGraph,
                                                                       final V nodeToReplace,
                                                                       final Graph<V, E> replacementTree,
                                                                       final EdgeFactory<V, E> edgeFactory,
                                                                       final Graph<V, E> resultGraph) {
        GraphUtils
                .getRootNode(replacementTree)
                .ifPresent(
                        replacementRoot -> {
                            copyGraphFromSrc2Dst(replacementTree, resultGraph, edgeFactory);
                            connectParentToRootOfReplacementTree(
                                    getParentAndIncomingEdge(originalGraph, nodeToReplace),
                                    edgeFactory,
                                    resultGraph,
                                    replacementRoot);
                        });
    }

    private record ParentAndEdge<V, E>(V parent, E edgeToChild) {
    }

    private static <V, E> void connectParentToRootOfReplacementTree(final Optional<ParentAndEdge<V, E>> parentAndEdge,
                                                                    final EdgeFactory<V, E> edgeFactory,
                                                                    final Graph<V, E> resultGraph,
                                                                    final V replacementRoot) {
        parentAndEdge
                .filter(_parentAndEdge -> resultGraph.containsVertex(_parentAndEdge.parent))
                .ifPresent(_parentAndEdge -> connectParentToRootOfReplacementTree(_parentAndEdge, edgeFactory, resultGraph, replacementRoot));
    }

    private static <V, E> void connectParentToRootOfReplacementTree(final ParentAndEdge<V, E> parentAndEdge,
                                                                    final EdgeFactory<V, E> edgeFactory,
                                                                    final Graph<V, E> resultGraph,
                                                                    final V replacementRoot) {
        resultGraph.addEdge(
                parentAndEdge.parent,
                replacementRoot,
                edgeFactory.createEdge(
                        parentAndEdge.parent,
                        replacementRoot,
                        parentAndEdge.edgeToChild));
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
        return ImmutableSet.copyOf(new BreadthFirstIterator<>(graph, startNode));
    }
}