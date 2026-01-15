package de.KnollFrank.lib.settingssearch.graph;

import com.google.common.collect.MoreCollectors;
import com.google.common.graph.EndpointPair;
import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.MutableValueGraph;

import de.KnollFrank.lib.settingssearch.common.graph.Edge;
import de.KnollFrank.lib.settingssearch.common.graph.Graphs;
import de.KnollFrank.lib.settingssearch.common.graph.NodeReplacer;
import de.KnollFrank.lib.settingssearch.common.graph.Subtree;
import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.common.graph.TreeAtNode;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
// FK-TODO: vereinfachen ähnlich zu SubtreeReplacer
public class TreeMerger {

    public static Tree<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> mergeSubtreeIntoTreeAtMergePoint(
            final Subtree<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> subtree,
            final TreeAtNode<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> treeAtMergePoint) {
        final TreeAtNode<SearchablePreferenceScreen, SearchablePreference, MutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> mergedTreeAtMergePoint =
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
        return new Tree<>(ImmutableValueGraph.copyOf(mergedTreeAtMergePoint.tree().graph()));
    }

    private record TreeAndEdge(
            Tree<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> tree,
            // FK-TODO: use Edge record?
            EndpointPair<SearchablePreferenceScreen> edge) {
    }

    private static void copySubtreesOfSrcToDst(final TreeAtNode<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> src,
                                               final TreeAtNode<SearchablePreferenceScreen, SearchablePreference, MutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> dst) {
        for (final Edge<SearchablePreferenceScreen, SearchablePreference> outgoingEdgeOfMergePoint : src.tree().outgoingEdgesOf(src.nodeOfTree())) {
            copyEdgeTargetSubtreeOfSrcToDst(
                    new TreeAndEdge(src.tree(), outgoingEdgeOfMergePoint.endpointPair()),
                    dst);
        }
    }

    private static void copyEdgeTargetSubtreeOfSrcToDst(final TreeAndEdge src,
                                                        final TreeAtNode<SearchablePreferenceScreen, SearchablePreference, MutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> dst) {
        final Subtree<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> srcSubtree = getEdgeTargetAsSubtree(src);
        copyNodesAndEdges(srcSubtree, dst.tree().graph());
        addEdgeFromMergePointToSubtree(
                src.tree().graph().edgeValueOrDefault(src.edge(), null),
                dst,
                srcSubtree);
    }

    private static Subtree<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> getEdgeTargetAsSubtree(final TreeAndEdge treeAndEdge) {
        return new Subtree<>(
                treeAndEdge.tree(),
                treeAndEdge.edge().target());
    }

    private static void addEdgeFromMergePointToSubtree(final SearchablePreference edgeValue,
                                                       final TreeAtNode<SearchablePreferenceScreen, SearchablePreference, MutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> mergePoint,
                                                       final Subtree<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> subtree) {
        final MutableValueGraph<SearchablePreferenceScreen, SearchablePreference> graph = mergePoint.tree().graph();
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

    private static void copyNodesAndEdges(final Subtree<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> src,
                                          final MutableValueGraph<SearchablePreferenceScreen, SearchablePreference> dst) {
        copyNodesOfSubtreeToGraph(src, dst);
        copyEdges(src.tree().graph(), dst);
    }

    private static void copyNodesOfSubtreeToGraph(final Subtree<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> subtree,
                                                  final MutableValueGraph<SearchablePreferenceScreen, SearchablePreference> graph) {
        subtree.getSubtreeNodes().forEach(graph::addNode);
    }

    private static void copyEdges(final ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference> src,
                                  final MutableValueGraph<SearchablePreferenceScreen, SearchablePreference> dst) {
        for (final EndpointPair<SearchablePreferenceScreen> edge : src.edges()) {
            copyEdge(edge, src, dst);
        }
    }

    private static void copyEdge(final EndpointPair<SearchablePreferenceScreen> edge,
                                 final ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference> src,
                                 final MutableValueGraph<SearchablePreferenceScreen, SearchablePreference> dst) {
        if (dst.nodes().contains(edge.source()) && dst.nodes().contains(edge.target()) && !dst.hasEdgeConnecting(edge)) {
            addEdgeHavingPreferenceFromSource(
                    src.edgeValueOrDefault(edge, null),
                    edge,
                    dst);
        }
    }

    private static <N, V> Tree<N, V, MutableValueGraph<N, V>> toMutableTree(final Tree<N, V, ImmutableValueGraph<N, V>> tree) {
        return new Tree<>(Graphs.toMutableValueGraph(tree.graph()));
    }
}
