package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import org.jgrapht.Graph;
import org.jgrapht.traverse.BreadthFirstIterator;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class SubtreeReplacer<V, E> {

    private final Supplier<Graph<V, E>> emptyGraphSupplier;
    private final Function<E, E> cloneEdge;

    public SubtreeReplacer(final Supplier<Graph<V, E>> emptyGraphSupplier,
                           final Function<E, E> cloneEdge) {
        this.emptyGraphSupplier = emptyGraphSupplier;
        this.cloneEdge = cloneEdge;
    }

    public UnmodifiableTree<V, E> replaceSubtreeWithTree(final Subtree<V, E> subtreeToReplace,
                                                         final UnmodifiableTree<V, E> replacementTree) {
        final Graph<V, E> resultGraph = emptyGraphSupplier.get();
        copyPartsOfGraph(
                subtreeToReplace.tree().graph(),
                getSubtreeVertices(subtreeToReplace),
                resultGraph);
        integrateReplacementTreeIntoResultGraph(
                subtreeToReplace.asGraphAtNode(),
                replacementTree.graph(),
                resultGraph);
        return UnmodifiableTree.of(resultGraph);
    }

    private void integrateReplacementTreeIntoResultGraph(final GraphAtNode<V, E> originalGraphAtNodeToReplace,
                                                         final Graph<V, E> replacementTree,
                                                         final Graph<V, E> resultGraph) {
        Graphs
                .getRootNode(replacementTree)
                .ifPresent(
                        replacementRoot -> {
                            copyGraphFromSrc2Dst(replacementTree, resultGraph);
                            connectParentToRootOfReplacementTree(
                                    getParentAndIncomingEdge(originalGraphAtNodeToReplace),
                                    resultGraph,
                                    replacementRoot);
                        });
    }

    private record ParentAndEdge<V, E>(V parent, E edgeToChild) {
    }

    private void connectParentToRootOfReplacementTree(final Optional<ParentAndEdge<V, E>> parentAndEdge,
                                                      final Graph<V, E> resultGraph,
                                                      final V replacementRoot) {
        parentAndEdge
                .filter(_parentAndEdge ->
                                resultGraph.containsVertex(_parentAndEdge.parent) &&
                                        !resultGraph.containsEdge(_parentAndEdge.parent, replacementRoot))
                .ifPresent(_parentAndEdge ->
                                   connectParentToRootOfReplacementTree(
                                           _parentAndEdge,
                                           resultGraph,
                                           replacementRoot));
    }

    private void connectParentToRootOfReplacementTree(final ParentAndEdge<V, E> parentAndEdge,
                                                      final Graph<V, E> resultGraph,
                                                      final V replacementRoot) {
        resultGraph.addEdge(
                parentAndEdge.parent,
                replacementRoot,
                cloneEdge.apply(parentAndEdge.edgeToChild));
    }

    private void copyGraphFromSrc2Dst(final Graph<V, E> src, final Graph<V, E> dst) {
        addNodesToGraph(src.vertexSet(), dst);
        copyEdgesFromSrc2Dst(src, src.edgeSet(), dst);
    }

    private void copyPartsOfGraph(final Graph<V, E> originalGraph,
                                  final Set<V> subtreeVerticesToRemove,
                                  final Graph<V, E> resultGraph) {
        addNodesToGraph(
                Sets.difference(originalGraph.vertexSet(), subtreeVerticesToRemove),
                resultGraph);
        copyEdgesFromSrc2Dst(
                originalGraph,
                getEdgesToRetain(originalGraph, subtreeVerticesToRemove),
                resultGraph);
    }

    private void addNodesToGraph(final Set<V> nodes, final Graph<V, E> graph) {
        nodes.forEach(graph::addVertex);
    }

    private void copyEdgesFromSrc2Dst(final Graph<V, E> src,
                                      final Set<E> edgesOfSrcToCopy,
                                      final Graph<V, E> dst) {
        for (final E edge : edgesOfSrcToCopy) {
            final V source = src.getEdgeSource(edge);
            final V target = src.getEdgeTarget(edge);
            if (!dst.containsEdge(source, target) && !nodeHasParent(dst, target)) {
                dst.addEdge(source, target, cloneEdge.apply(edge));
            }
        }
    }

    private static <V, E> boolean nodeHasParent(final Graph<V, E> graph, final V node) {
        return graph.containsVertex(node) && !graph.incomingEdgesOf(node).isEmpty();
    }

    private Set<E> getEdgesToRetain(final Graph<V, E> graph, final Set<V> nodesToRemove) {
        return graph
                .edgeSet()
                .stream()
                .filter(edge -> !nodesToRemove.contains(graph.getEdgeSource(edge)) &&
                        !nodesToRemove.contains(graph.getEdgeTarget(edge)))
                .collect(Collectors.toSet());
    }

    private Optional<ParentAndEdge<V, E>> getParentAndIncomingEdge(final GraphAtNode<V, E> graphAtNode) {
        return Graphs
                .getIncomingEdgeOfNode(
                        graphAtNode.graph(),
                        graphAtNode.nodeOfGraph())
                .map(edgeToChild ->
                             new ParentAndEdge<>(
                                     graphAtNode.graph().getEdgeSource(edgeToChild),
                                     edgeToChild));
    }

    private Set<V> getSubtreeVertices(final Subtree<V, E> subtree) {
        return ImmutableSet.copyOf(
                new BreadthFirstIterator<>(
                        subtree.tree().graph(),
                        subtree.rootNodeOfSubtree()));
    }
}
