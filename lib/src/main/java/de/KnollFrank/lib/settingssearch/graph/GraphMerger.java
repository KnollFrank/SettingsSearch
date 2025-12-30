package de.KnollFrank.lib.settingssearch.graph;

import org.jgrapht.Graph;
import org.jgrapht.traverse.BreadthFirstIterator;

import java.util.Set;

import de.KnollFrank.lib.settingssearch.common.graph.Graphs;
import de.KnollFrank.lib.settingssearch.common.graph.SearchablePreferenceScreenNodeReplacerFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

public class GraphMerger {

    public record GraphAtMergePoint(
            Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph,
            SearchablePreferenceScreen mergePointOfGraph) {

        public Set<SearchablePreferenceEdge> outgoingEdgesOfMergePoint() {
            return graph.outgoingEdgesOf(mergePointOfGraph);
        }
    }

    public static Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> mergeSrcGraphIntoDstGraphAtMergePoint(
            final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> srcGraph,
            final GraphAtMergePoint dstGraphAtMergePoint) {
        return mergeSubtreeIntoGraphAtMergePoint(
                asSubtree(srcGraph),
                dstGraphAtMergePoint);
    }

    private static Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> mergeSubtreeIntoGraphAtMergePoint(
            final Subtree subtree,
            final GraphAtMergePoint graphAtMergePoint) {
        final GraphAtMergePoint mergedGraphAtMergePoint =
                new GraphAtMergePoint(
                        SearchablePreferenceScreenNodeReplacerFactory
                                .createNodeReplacer()
                                .replaceNode(
                                        graphAtMergePoint.graph(),
                                        graphAtMergePoint.mergePointOfGraph(),
                                        subtree.subtreeRoot()),
                        subtree.subtreeRoot());
        // Ursprüngliche Kinder des Merge-Points wieder an die neue Wurzel hängen.
        copySubtreesOfSrcToDst(graphAtMergePoint, mergedGraphAtMergePoint);
        // Kinder aus dem Teilgraphen an die neue Wurzel hängen.
        copySubtreesOfSrcToDst(subtree.asGraphAtMergePoint(), mergedGraphAtMergePoint);
        return mergedGraphAtMergePoint.graph();
    }

    private record Subtree(Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph,
                           SearchablePreferenceScreen subtreeRoot) {

        public GraphAtMergePoint asGraphAtMergePoint() {
            return new GraphAtMergePoint(graph, subtreeRoot);
        }
    }

    private static Subtree asSubtree(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph) {
        return new Subtree(
                graph,
                Graphs.getRootNode(graph).orElseThrow());
    }

    private record GraphAndEdge(Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph,
                                SearchablePreferenceEdge edge) {
    }

    private static void copySubtreesOfSrcToDst(final GraphAtMergePoint src, final GraphAtMergePoint dst) {
        for (final SearchablePreferenceEdge outgoingEdgeOfMergePoint : src.outgoingEdgesOfMergePoint()) {
            copyEdgeTargetSubtreeOfSrcToDst(
                    new GraphAndEdge(src.graph(), outgoingEdgeOfMergePoint),
                    dst);
        }
    }

    private static void copyEdgeTargetSubtreeOfSrcToDst(final GraphAndEdge src, final GraphAtMergePoint dst) {
        final Subtree srcSubtree = getEdgeTargetAsSubtree(src);
        copyNodesAndEdges(srcSubtree, dst.graph());
        addEdgeFromMergePointToSubtree(src.edge(), dst, srcSubtree);
    }

    private static Subtree getEdgeTargetAsSubtree(final GraphAndEdge graphAndEdge) {
        return new Subtree(
                graphAndEdge.graph(),
                graphAndEdge.graph().getEdgeTarget(graphAndEdge.edge()));
    }

    private static void addEdgeFromMergePointToSubtree(final SearchablePreferenceEdge edge,
                                                       final GraphAtMergePoint mergePoint,
                                                       final Subtree subtree) {
        final SearchablePreferenceScreen sourceVertex = mergePoint.mergePointOfGraph();
        final SearchablePreferenceScreen targetVertex = subtree.subtreeRoot();
        final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph = mergePoint.graph();
        if (!graph.containsEdge(sourceVertex, targetVertex)) {
            graph.addEdge(sourceVertex, targetVertex, edge);
        }
    }

    private static void copyNodesAndEdges(final Subtree src,
                                          final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> dst) {
        copyNodesOfSubtreeToGraph(src, dst);
        copyEdges(src.graph(), dst);
    }

    private static void copyNodesOfSubtreeToGraph(final Subtree subtree,
                                                  final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph) {
        new BreadthFirstIterator<>(subtree.graph(), subtree.subtreeRoot())
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
