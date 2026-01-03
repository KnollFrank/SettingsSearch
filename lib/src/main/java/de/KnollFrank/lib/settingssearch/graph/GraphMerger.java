package de.KnollFrank.lib.settingssearch.graph;

import org.jgrapht.Graph;
import org.jgrapht.traverse.BreadthFirstIterator;

import de.KnollFrank.lib.settingssearch.common.graph.GraphAtNode;
import de.KnollFrank.lib.settingssearch.common.graph.SearchablePreferenceScreenNodeReplacerFactory;
import de.KnollFrank.lib.settingssearch.common.graph.Subtree;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

public class GraphMerger {

    public static Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> mergeSrcGraphIntoDstGraphAtMergePoint(
            final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> srcGraph,
            final GraphAtNode<SearchablePreferenceScreen, SearchablePreferenceEdge> dstGraphAtMergePoint) {
        return mergeSubtreeIntoGraphAtMergePoint(
                Subtree.of(srcGraph),
                dstGraphAtMergePoint);
    }

    private static Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> mergeSubtreeIntoGraphAtMergePoint(
            final Subtree<SearchablePreferenceScreen, SearchablePreferenceEdge> subtree,
            final GraphAtNode<SearchablePreferenceScreen, SearchablePreferenceEdge> graphAtMergePoint) {
        final GraphAtNode<SearchablePreferenceScreen, SearchablePreferenceEdge> mergedGraphAtMergePoint =
                new GraphAtNode<>(
                        SearchablePreferenceScreenNodeReplacerFactory
                                .createNodeReplacer()
                                .replaceNode(
                                        graphAtMergePoint,
                                        subtree.rootNodeOfSubtree()),
                        subtree.rootNodeOfSubtree());
        // Ursprüngliche Kinder des Merge-Points wieder an die neue Wurzel hängen.
        copySubtreesOfSrcToDst(graphAtMergePoint, mergedGraphAtMergePoint);
        // Kinder aus dem Teilgraphen an die neue Wurzel hängen.
        copySubtreesOfSrcToDst(subtree.asGraphAtNode(), mergedGraphAtMergePoint);
        return mergedGraphAtMergePoint.graph();
    }

    private record GraphAndEdge(Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph,
                                SearchablePreferenceEdge edge) {
    }

    private static void copySubtreesOfSrcToDst(final GraphAtNode<SearchablePreferenceScreen, SearchablePreferenceEdge> src,
                                               final GraphAtNode<SearchablePreferenceScreen, SearchablePreferenceEdge> dst) {
        for (final SearchablePreferenceEdge outgoingEdgeOfMergePoint : src.outgoingEdgesOfNodeOfGraph()) {
            copyEdgeTargetSubtreeOfSrcToDst(
                    new GraphAndEdge(src.graph(), outgoingEdgeOfMergePoint),
                    dst);
        }
    }

    private static void copyEdgeTargetSubtreeOfSrcToDst(final GraphAndEdge src,
                                                        final GraphAtNode<SearchablePreferenceScreen, SearchablePreferenceEdge> dst) {
        final Subtree<SearchablePreferenceScreen, SearchablePreferenceEdge> srcSubtree = getEdgeTargetAsSubtree(src);
        copyNodesAndEdges(srcSubtree, dst.graph());
        addEdgeFromMergePointToSubtree(src.edge(), dst, srcSubtree);
    }

    private static Subtree<SearchablePreferenceScreen, SearchablePreferenceEdge> getEdgeTargetAsSubtree(final GraphAndEdge graphAndEdge) {
        return new Subtree<>(
                graphAndEdge.graph(),
                graphAndEdge.graph().getEdgeTarget(graphAndEdge.edge()));
    }

    private static void addEdgeFromMergePointToSubtree(final SearchablePreferenceEdge edge,
                                                       final GraphAtNode<SearchablePreferenceScreen, SearchablePreferenceEdge> mergePoint,
                                                       final Subtree<SearchablePreferenceScreen, SearchablePreferenceEdge> subtree) {
        final SearchablePreferenceScreen sourceVertex = mergePoint.nodeOfGraph();
        final SearchablePreferenceScreen targetVertex = subtree.rootNodeOfSubtree();
        final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph = mergePoint.graph();
        if (!graph.containsEdge(sourceVertex, targetVertex)) {
            graph.addEdge(sourceVertex, targetVertex, edge);
        }
    }

    private static void copyNodesAndEdges(final Subtree<SearchablePreferenceScreen, SearchablePreferenceEdge> src,
                                          final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> dst) {
        copyNodesOfSubtreeToGraph(src, dst);
        copyEdges(src.tree(), dst);
    }

    private static void copyNodesOfSubtreeToGraph(final Subtree<SearchablePreferenceScreen, SearchablePreferenceEdge> subtree,
                                                  final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph) {
        new BreadthFirstIterator<>(subtree.tree(), subtree.rootNodeOfSubtree())
                .forEachRemaining(graph::addVertex);
    }

    private static void copyEdges(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> src,
                                  final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> dst) {
        for (final SearchablePreferenceEdge edge : src.edgeSet()) {
            copyEdge(edge, src, dst);
        }
    }

    private static void copyEdge(final SearchablePreferenceEdge edge,
                                 final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> src,
                                 final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> dst) {
        final SearchablePreferenceScreen sourceNode = src.getEdgeSource(edge);
        final SearchablePreferenceScreen targetNode = src.getEdgeTarget(edge);
        if (dst.containsVertex(sourceNode) && dst.containsVertex(targetNode) && !dst.containsEdge(sourceNode, targetNode)) {
            dst.addEdge(sourceNode, targetNode, edge);
        }
    }
}
