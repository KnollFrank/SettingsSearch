package de.KnollFrank.lib.settingssearch.common.graph;

import static de.KnollFrank.lib.settingssearch.common.graph.PredecessorOfPreferencesOfNodeSetter.setPredecessorOfPreferencesOfNode;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;

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
}
