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

    public Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> mergeSrcGraphWithDstGraph(
            final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> srcGraph,
            final GraphAndMergePoint dstGraphAndMergePoint) {
        final SearchablePreferenceScreen rootOfPartialGraph = Graphs.getRootNode(srcGraph).orElseThrow();
        final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> mergedGraph =
                SearchablePreferenceScreenNodeReplacer.replaceNode(
                        dstGraphAndMergePoint.graph(),
                        dstGraphAndMergePoint.mergePointOfGraph(),
                        rootOfPartialGraph);

        // 3. Teilbäume umhängen.
        // Ursprüngliche Kinder des Merge-Points wieder an die neue Wurzel hängen.
        extracted(dstGraphAndMergePoint, new GraphAndMergePoint(mergedGraph, rootOfPartialGraph));
        // Kinder aus dem Teilgraphen an die neue Wurzel hängen.
        extracted(new GraphAndMergePoint(srcGraph, rootOfPartialGraph), new GraphAndMergePoint(mergedGraph, rootOfPartialGraph));
        return mergedGraph;
    }

    private record GraphAndEdge(Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph,
                                SearchablePreferenceEdge edge) {
    }

    private void extracted(final GraphAndMergePoint src, final GraphAndMergePoint dst) {
        for (final SearchablePreferenceEdge srcEdge : src.graph().outgoingEdgesOf(src.mergePointOfGraph())) {
            attachSubtree(new GraphAndEdge(src.graph(), srcEdge), dst);
        }
    }

    private void attachSubtree(final GraphAndEdge src, final GraphAndMergePoint dst) {
        final Subtree srcSubtree =
                new Subtree(
                        src.graph(),
                        src.graph().getEdgeTarget(src.edge()));
        copyNodesAndEdges(srcSubtree, dst);
        addEdgeFromMergePointToSubtree(dst, src, srcSubtree);
    }

    private static void addEdgeFromMergePointToSubtree(final GraphAndMergePoint mergePoint,
                                                       final GraphAndEdge src,
                                                       final Subtree subtree) {
        if (!mergePoint.graph().containsEdge(mergePoint.mergePointOfGraph(), subtree.subtreeRoot())) {
            mergePoint.graph().addEdge(mergePoint.mergePointOfGraph(), subtree.subtreeRoot(), src.edge());
        }
    }

    private record Subtree(Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph,
                           SearchablePreferenceScreen subtreeRoot) {
    }

    private static void copyNodesAndEdges(final Subtree src, final GraphAndMergePoint dst) {
        copyNodesOfSubtreeToGraph(src, dst.graph());
        copyEdges(src.graph(), dst.graph());
    }

    private static void copyNodesOfSubtreeToGraph(final Subtree src,
                                                  final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> dst) {
        new BreadthFirstIterator<>(src.graph(), src.subtreeRoot())
                .forEachRemaining(
                        node -> {
                            if (!dst.containsVertex(node)) {
                                dst.addVertex(node);
                            }
                        });
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
