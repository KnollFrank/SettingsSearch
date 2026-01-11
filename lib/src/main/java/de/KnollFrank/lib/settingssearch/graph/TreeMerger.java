package de.KnollFrank.lib.settingssearch.graph;

import com.google.common.collect.MoreCollectors;
import com.google.common.graph.EndpointPair;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraph;

import de.KnollFrank.lib.settingssearch.common.graph.NodeReplacer;
import de.KnollFrank.lib.settingssearch.common.graph.Subtree;
import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.common.graph.TreeAtNode;
import de.KnollFrank.lib.settingssearch.common.graph.ValueGraphs;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class TreeMerger {

    public static Tree<SearchablePreferenceScreen, SearchablePreference> mergeSubtreeIntoTreeAtMergePoint(
            final Subtree<SearchablePreferenceScreen, SearchablePreference> subtree,
            final TreeAtNode<SearchablePreferenceScreen, SearchablePreference> treeAtMergePoint) {
        final TreeAtNode<SearchablePreferenceScreen, SearchablePreference> mergedTreeAtMergePoint =
                new TreeAtNode<>(
                        toMutableTree(
                                NodeReplacer.replaceNode(
                                        treeAtMergePoint,
                                        subtree.rootNodeOfSubtree())),
                        subtree.rootNodeOfSubtree());
        // Re-attach original children of the merge point to the new root.
        copySubtreesOfSrcToDst(treeAtMergePoint, mergedTreeAtMergePoint);
        // Attach children from the partial graph to the new root.
        copySubtreesOfSrcToDst(subtree.asTreeAtNode(), mergedTreeAtMergePoint);
        return mergedTreeAtMergePoint.tree();
    }

    private record TreeAndEdge(Tree<SearchablePreferenceScreen, SearchablePreference> tree,
                               EndpointPair<SearchablePreferenceScreen> edge) {
    }

    private static void copySubtreesOfSrcToDst(final TreeAtNode<SearchablePreferenceScreen, SearchablePreference> src,
                                               final TreeAtNode<SearchablePreferenceScreen, SearchablePreference> dst) {
        for (final EndpointPair<SearchablePreferenceScreen> outgoingEdgeOfMergePoint : src.tree().outgoingEdgesOf(src.nodeOfTree())) {
            copyEdgeTargetSubtreeOfSrcToDst(
                    new TreeAndEdge(src.tree(), outgoingEdgeOfMergePoint),
                    dst);
        }
    }

    private static void copyEdgeTargetSubtreeOfSrcToDst(final TreeAndEdge src,
                                                        final TreeAtNode<SearchablePreferenceScreen, SearchablePreference> dst) {
        final Subtree<SearchablePreferenceScreen, SearchablePreference> srcSubtree = getEdgeTargetAsSubtree(src);
        // FK-FIXME: casting is bad!
        copyNodesAndEdges(srcSubtree, (MutableValueGraph<SearchablePreferenceScreen, SearchablePreference>) dst.tree().graph());
        addEdgeFromMergePointToSubtree(
                src.tree().graph().edgeValueOrDefault(src.edge(), null),
                dst,
                srcSubtree);
    }

    private static Subtree<SearchablePreferenceScreen, SearchablePreference> getEdgeTargetAsSubtree(final TreeAndEdge treeAndEdge) {
        return new Subtree<>(
                treeAndEdge.tree(),
                treeAndEdge.edge().target());
    }

    private static void addEdgeFromMergePointToSubtree(final SearchablePreference edgeValue,
                                                       final TreeAtNode<SearchablePreferenceScreen, SearchablePreference> mergePoint,
                                                       final Subtree<SearchablePreferenceScreen, SearchablePreference> subtree) {
        // FK-FIXME: casting is bad!
        final MutableValueGraph<SearchablePreferenceScreen, SearchablePreference> graph = (MutableValueGraph<SearchablePreferenceScreen, SearchablePreference>) mergePoint.tree().graph();
        final EndpointPair<SearchablePreferenceScreen> edge =
                EndpointPair.ordered(
                        mergePoint.nodeOfTree(),
                        subtree.rootNodeOfSubtree());
        if (!graph.hasEdgeConnecting(edge)) {
            addEdgeHavingPreferenceFromSource(edgeValue, edge, graph);
        }
    }

    private static void addEdgeHavingPreferenceFromSource(
            final SearchablePreference edgeValue,
            final EndpointPair<SearchablePreferenceScreen> edge,
            final MutableValueGraph<SearchablePreferenceScreen, SearchablePreference> graph) {
        graph.putEdgeValue(
                edge,
                getPreferenceFromScreen(edge.source(), edgeValue));
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

    private static void copyNodesAndEdges(final Subtree<SearchablePreferenceScreen, SearchablePreference> src,
                                          final MutableValueGraph<SearchablePreferenceScreen, SearchablePreference> dst) {
        copyNodesOfSubtreeToGraph(src, dst);
        copyEdges(src.tree().graph(), dst);
    }

    private static void copyNodesOfSubtreeToGraph(final Subtree<SearchablePreferenceScreen, SearchablePreference> subtree,
                                                  final MutableValueGraph<SearchablePreferenceScreen, SearchablePreference> graph) {
        subtree.getSubtreeNodes().forEach(graph::addNode);
    }

    private static void copyEdges(final ValueGraph<SearchablePreferenceScreen, SearchablePreference> src,
                                  final MutableValueGraph<SearchablePreferenceScreen, SearchablePreference> dst) {
        for (final EndpointPair<SearchablePreferenceScreen> edge : src.edges()) {
            copyEdge(edge, src, dst);
        }
    }

    private static void copyEdge(final EndpointPair<SearchablePreferenceScreen> edge,
                                 final ValueGraph<SearchablePreferenceScreen, SearchablePreference> src,
                                 final MutableValueGraph<SearchablePreferenceScreen, SearchablePreference> dst) {
        if (dst.nodes().contains(edge.source()) && dst.nodes().contains(edge.target()) && !dst.hasEdgeConnecting(edge)) {
            addEdgeHavingPreferenceFromSource(
                    src.edgeValueOrDefault(edge, null),
                    edge,
                    dst);
        }
    }

    private static <N, V> Tree<N, V> toMutableTree(final Tree<N, V> tree) {
        return new Tree<>(ValueGraphs.toMutableValueGraph(tree.graph()));
    }
}
