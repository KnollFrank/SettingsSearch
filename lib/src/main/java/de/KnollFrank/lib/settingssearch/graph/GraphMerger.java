package de.KnollFrank.lib.settingssearch.graph;

import org.jgrapht.Graph;
import org.jgrapht.traverse.BreadthFirstIterator;

import de.KnollFrank.lib.settingssearch.common.graph.Graphs;
import de.KnollFrank.lib.settingssearch.common.graph.SearchablePreferenceScreenNodeReplacer;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

public class GraphMerger {

    public record GraphAndMergePoint(
            Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph,
            SearchablePreferenceScreen mergePointOfGraph) {
    }

    public Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> mergeSrcGraphIntoDstGraph(
            final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> srcGraph,
            final GraphAndMergePoint dstGraphAndMergePoint) {
        final SearchablePreferenceScreen rootOfSrcGraph = Graphs.getRootNode(srcGraph).orElseThrow();
        final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> mergedGraph =
                SearchablePreferenceScreenNodeReplacer.replaceNode(
                        dstGraphAndMergePoint.graph(),
                        dstGraphAndMergePoint.mergePointOfGraph(),
                        rootOfSrcGraph);

        // 3. Teilbäume umhängen.
        // Ursprüngliche Kinder des Merge-Points wieder an die neue Wurzel hängen.
        final GraphAndMergePoint dst = new GraphAndMergePoint(mergedGraph, rootOfSrcGraph);
        attachSubtree(dstGraphAndMergePoint, dst);
        // Kinder aus dem Teilgraphen an die neue Wurzel hängen.
        attachSubtree(new GraphAndMergePoint(srcGraph, rootOfSrcGraph), dst);
        return mergedGraph;
    }

    private record GraphAndEdge(Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph,
                                SearchablePreferenceEdge edge) {
    }

    private void attachSubtree(final GraphAndMergePoint src, final GraphAndMergePoint dst) {
        for (final SearchablePreferenceEdge srcEdge : src.graph().outgoingEdgesOf(src.mergePointOfGraph())) {
            attachSubtree(new GraphAndEdge(src.graph(), srcEdge), dst);
        }
    }

    private void attachSubtree(final GraphAndEdge src, final GraphAndMergePoint dst) {
        final Subtree srcSubtree =
                new Subtree(
                        src.graph(),
                        src.graph().getEdgeTarget(src.edge()));
        copyNodesAndEdges(srcSubtree, dst.graph());
        addEdgeFromMergePointToSubtree(src.edge(), dst, srcSubtree);
    }

    private static void addEdgeFromMergePointToSubtree(final SearchablePreferenceEdge edge,
                                                       final GraphAndMergePoint mergePoint,
                                                       final Subtree subtree) {
        final SearchablePreferenceScreen sourceVertex = mergePoint.mergePointOfGraph();
        final SearchablePreferenceScreen targetVertex = subtree.subtreeRoot();
        final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph = mergePoint.graph();
        if (!graph.containsEdge(sourceVertex, targetVertex)) {
            graph.addEdge(sourceVertex, targetVertex, edge);
        }
    }

    private record Subtree(Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph,
                           SearchablePreferenceScreen subtreeRoot) {
    }

    // FK-TODO: SubtreeReplacer verwenden?
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
