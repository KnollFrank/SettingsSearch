package de.KnollFrank.lib.settingssearch.graph;

import com.google.common.collect.MoreCollectors;

import org.jgrapht.Graph;
import org.jgrapht.traverse.BreadthFirstIterator;

import de.KnollFrank.lib.settingssearch.common.graph.GraphAtNode;
import de.KnollFrank.lib.settingssearch.common.graph.SearchablePreferenceScreenNodeReplacerFactory;
import de.KnollFrank.lib.settingssearch.common.graph.Subtree;
import de.KnollFrank.lib.settingssearch.common.graph.UnmodifiableTree;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

public class GraphMerger {

    public static Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> mergeSubtreeIntoGraphAtMergePoint(
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
        // Re-attach original children of the merge point to the new root.
        copySubtreesOfSrcToDst(graphAtMergePoint, mergedGraphAtMergePoint);
        // Attach children from the partial graph to the new root.
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
                UnmodifiableTree.of(graphAndEdge.graph()),
                graphAndEdge.graph().getEdgeTarget(graphAndEdge.edge()));
    }

    private static void addEdgeFromMergePointToSubtree(final SearchablePreferenceEdge edge,
                                                       final GraphAtNode<SearchablePreferenceScreen, SearchablePreferenceEdge> mergePoint,
                                                       final Subtree<SearchablePreferenceScreen, SearchablePreferenceEdge> subtree) {
        final SearchablePreferenceScreen sourceVertex = mergePoint.nodeOfGraph();
        final SearchablePreferenceScreen targetVertex = subtree.rootNodeOfSubtree();
        final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph = mergePoint.graph();
        if (!graph.containsEdge(sourceVertex, targetVertex)) {
            addEdgeHavingPreferenceFromSource(edge, sourceVertex, targetVertex, graph);
        }
    }

    private static void addEdgeHavingPreferenceFromSource(
            final SearchablePreferenceEdge edge,
            final SearchablePreferenceScreen source,
            final SearchablePreferenceScreen target,
            final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph) {
        graph.addEdge(
                source,
                target,
                new SearchablePreferenceEdge(
                        getPreferenceFromScreen(
                                source,
                                edge.preference)));
    }

    private static SearchablePreference getPreferenceFromScreen(final SearchablePreferenceScreen screen,
                                                                final SearchablePreference preference) {
        return screen
                .allPreferencesOfPreferenceHierarchy()
                .stream()
                .filter(_preference -> _preference.getKey().equals(preference.getKey()))
                .collect(MoreCollectors.toOptional())
                .orElseThrow(
                        () ->
                                new IllegalStateException(
                                        String.format("Integrity error: Preference with key '%s' not found in screen '%s'.",
                                                      preference.getKey(),
                                                      screen.id())));
    }

    private static void copyNodesAndEdges(final Subtree<SearchablePreferenceScreen, SearchablePreferenceEdge> src,
                                          final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> dst) {
        copyNodesOfSubtreeToGraph(src, dst);
        copyEdges(src.tree().graph(), dst);
    }

    private static void copyNodesOfSubtreeToGraph(final Subtree<SearchablePreferenceScreen, SearchablePreferenceEdge> subtree,
                                                  final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph) {
        new BreadthFirstIterator<>(subtree.tree().graph(), subtree.rootNodeOfSubtree())
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
            addEdgeHavingPreferenceFromSource(edge, sourceNode, targetNode, dst);
        }
    }
}
