package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.collect.Iterables;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

public class SearchablePreferenceScreenSubtreeReplacer {

    public static Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> replaceSubtreeWithTree(
            final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> originalGraph,
            final SearchablePreferenceScreen rootNodeOfSubtreeToReplace,
            final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> replacementTree) {
        final var resultGraph = createSubtreeReplacer().replaceSubtreeWithTree(originalGraph, rootNodeOfSubtreeToReplace, replacementTree);
        setPredecessorOfPreferencesOfNode(
                resultGraph,
                Graphs.getRootNode(replacementTree).orElseThrow());
        return resultGraph;
    }

    private static SubtreeReplacer<SearchablePreferenceScreen, SearchablePreferenceEdge> createSubtreeReplacer() {
        return new SubtreeReplacer<>(
                () -> new DefaultDirectedGraph<>(SearchablePreferenceEdge.class),
                edge -> new SearchablePreferenceEdge(edge.preference));
    }

    private static void setPredecessorOfPreferencesOfNode(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph,
                                                          final SearchablePreferenceScreen node) {
        final SearchablePreference predecessor =
                SearchablePreferenceScreenSubtreeReplacer
                        .getIncomingEdgeOfNode(graph, node)
                        .preference;
        for (final SearchablePreference searchablePreference : node.allPreferencesOfPreferenceHierarchy()) {
            searchablePreference.setPredecessor(Optional.of(predecessor));
        }
    }

    private static SearchablePreferenceEdge getIncomingEdgeOfNode(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph,
                                                                  final SearchablePreferenceScreen node) {
        return Iterables.getOnlyElement(graph.incomingEdgesOf(node));
    }
}
