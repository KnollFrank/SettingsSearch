package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.collect.Sets;
import com.google.common.graph.EndpointPair;
import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraph;
import com.google.common.graph.ValueGraphBuilder;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class SubtreeReplacer {

    public static <N, V> Tree<N, V, ImmutableValueGraph<N, V>> replaceSubtreeWithTree(final Subtree<N, V, ImmutableValueGraph<N, V>> subtreeToReplace,
                                                                                      final Tree<N, V, ImmutableValueGraph<N, V>> replacementTree) {
        final MutableValueGraph<N, V> resultGraph = ValueGraphBuilder.directed().build();
        copyPartsOfGraph(
                subtreeToReplace.tree().graph(),
                subtreeToReplace.getSubtreeNodes(),
                resultGraph);
        integrateReplacementTreeIntoResultGraph(
                subtreeToReplace.asTreeAtNode(),
                replacementTree,
                resultGraph);
        return new Tree<>(ImmutableValueGraph.copyOf(resultGraph));
    }

    private static <N, V> void integrateReplacementTreeIntoResultGraph(final TreeAtNode<N, V, ImmutableValueGraph<N, V>> originalTreeAtNodeToReplace,
                                                                       final Tree<N, V, ImmutableValueGraph<N, V>> replacementTree,
                                                                       final MutableValueGraph<N, V> resultGraph) {
        copyGraphFromSrc2Dst(replacementTree.graph(), resultGraph);
        connectParentToRootOfReplacementTree(
                getParentAndEdgeValue(originalTreeAtNodeToReplace),
                resultGraph,
                replacementTree.rootNode());
    }

    private record ParentAndEdgeValue<N, V>(N parent, V valueOfEdgeToChild) {
    }

    private static <N, V> void connectParentToRootOfReplacementTree(final Optional<ParentAndEdgeValue<N, V>> parentAndEdgeValue,
                                                                    final MutableValueGraph<N, V> resultGraph,
                                                                    final N replacementRoot) {
        parentAndEdgeValue
                .filter(_parentAndEdgeValue ->
                        resultGraph.nodes().contains(_parentAndEdgeValue.parent) &&
                                !resultGraph.hasEdgeConnecting(_parentAndEdgeValue.parent, replacementRoot))
                .ifPresent(_parentAndEdgeValue ->
                        connectParentToRootOfReplacementTree(
                                _parentAndEdgeValue,
                                resultGraph,
                                replacementRoot));
    }

    private static <N, V> void connectParentToRootOfReplacementTree(final ParentAndEdgeValue<N, V> parentAndEdgeValue,
                                                                    final MutableValueGraph<N, V> resultGraph,
                                                                    final N replacementRoot) {
        resultGraph.putEdgeValue(
                parentAndEdgeValue.parent,
                replacementRoot,
                parentAndEdgeValue.valueOfEdgeToChild);
    }

    private static <N, V> void copyGraphFromSrc2Dst(final ImmutableValueGraph<N, V> src, final MutableValueGraph<N, V> dst) {
        addNodesToGraph(src.nodes(), dst);
        copyEdgesFromSrc2Dst(src, src.edges(), dst);
    }

    private static <N, V> void copyPartsOfGraph(final ImmutableValueGraph<N, V> originalGraph,
                                                final Set<N> subtreeVerticesToRemove,
                                                final MutableValueGraph<N, V> resultGraph) {
        addNodesToGraph(
                Sets.difference(originalGraph.nodes(), subtreeVerticesToRemove),
                resultGraph);
        copyEdgesFromSrc2Dst(
                originalGraph,
                getEdgesToRetain(originalGraph.edges(), subtreeVerticesToRemove),
                resultGraph);
    }

    private static <N, V> void addNodesToGraph(final Set<N> nodes, final MutableValueGraph<N, V> graph) {
        nodes.forEach(graph::addNode);
    }

    private static <N, V> void copyEdgesFromSrc2Dst(final ImmutableValueGraph<N, V> src,
                                                    final Set<EndpointPair<N>> edgesOfSrcToCopy,
                                                    final MutableValueGraph<N, V> dst) {
        for (final EndpointPair<N> edge : edgesOfSrcToCopy) {
            final N source = edge.source();
            final N target = edge.target();
            if (!dst.hasEdgeConnecting(edge) && !nodeHasParent(dst, target)) {
                dst.putEdgeValue(source, target, src.edgeValueOrDefault(edge, null));
            }
        }
    }

    private static <N, V> boolean nodeHasParent(final ValueGraph<N, V> graph, final N node) {
        return graph.nodes().contains(node) && !graph.predecessors(node).isEmpty();
    }

    private static <N> Set<EndpointPair<N>> getEdgesToRetain(final Set<EndpointPair<N>> edges, final Set<N> nodesToRemove) {
        return edges
                .stream()
                .filter(edge -> retainEdge(edge, nodesToRemove))
                .collect(Collectors.toSet());
    }

    private static <N> boolean retainEdge(final EndpointPair<N> edge, final Set<N> nodesToRemove) {
        return !nodesToRemove.contains(edge.source()) && !nodesToRemove.contains(edge.target());
    }


    private static <N, V> Optional<ParentAndEdgeValue<N, V>> getParentAndEdgeValue(final TreeAtNode<N, V, ImmutableValueGraph<N, V>> treeAtNode) {
        return treeAtNode
                .tree()
                .incomingEdgeOf(treeAtNode.nodeOfTree())
                .map(incomingEdge ->
                        new ParentAndEdgeValue<>(
                                incomingEdge.source(),
                                treeAtNode.tree().graph().edgeValueOrDefault(incomingEdge, null)));
    }
}
