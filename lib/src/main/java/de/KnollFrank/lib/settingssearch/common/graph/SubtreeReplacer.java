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

// FK-TODO: refactor
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

        final Set<V> subtreeVerticesToRemove = getSubtreeVertices(subtreeToReplace);

        // 1. Copy original parts that are NOT being replaced
        copyPartsOfGraph(
                subtreeToReplace.tree().graph(),
                subtreeVerticesToRemove,
                resultGraph);

        // 2. Integrate the new tree
        integrateReplacementTreeIntoResultGraph(subtreeToReplace.asGraphAtNode(), replacementTree.graph(), resultGraph);

        // 3. Validation happens here (Fail-Fast)
        return UnmodifiableTree.of(resultGraph);
    }

    private void integrateReplacementTreeIntoResultGraph(final GraphAtNode<V, E> originalGraphAtNodeToReplace,
                                                         final Graph<V, E> replacementTree,
                                                         final Graph<V, E> resultGraph) {
        Graphs.getRootNode(replacementTree).ifPresent(replacementRoot -> {
            // First, copy the replacement structure
            copyGraphFromSrc2Dst(replacementTree, resultGraph);

            // Then, connect the parent of the old node to the new replacement root
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
        // Linearized logic to avoid confusion and potential double insertion
        parentAndEdge.ifPresent(p -> {
            // Ensure parent exists and we don't duplicate a connection that might
            // have been added by copyGraphFromSrc2Dst already
            if (resultGraph.containsVertex(p.parent) && !resultGraph.containsEdge(p.parent, replacementRoot)) {
                resultGraph.addEdge(
                        p.parent,
                        replacementRoot,
                        cloneEdge.apply(p.edgeToChild));
            }
        });
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

            // 1. Existiert die exakte Kante schon? -> Überspringen
            if (dst.containsEdge(source, target)) {
                continue;
            }

            // 2. Hat der Zielknoten bereits einen anderen Vater?
            // Das ist der kritische Punkt für den "260 statt 257" Fehler!
            if (dst.containsVertex(target) && !dst.incomingEdgesOf(target).isEmpty()) {
                // Wenn der Knoten schon einen Vater hat, ignorieren wir diese Kante,
                // um die Baum-Struktur (In-Degree max 1) nicht zu verletzen.
                continue;
            }

            dst.addEdge(source, target, cloneEdge.apply(edge));
        }
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
        final Set<E> incomingEdges = graphAtNode.incomingEdgesOfNodeOfGraph();
        if (!incomingEdges.isEmpty()) {
            final E edgeToChild = incomingEdges.iterator().next();
            return Optional.of(
                    new ParentAndEdge<>(
                            graphAtNode.graph().getEdgeSource(edgeToChild),
                            edgeToChild));
        }
        return Optional.empty();
    }

    private Set<V> getSubtreeVertices(final Subtree<V, E> subtree) {
        return ImmutableSet.copyOf(
                new BreadthFirstIterator<>(
                        subtree.tree().graph(),
                        subtree.rootNodeOfSubtree()));
    }
}
