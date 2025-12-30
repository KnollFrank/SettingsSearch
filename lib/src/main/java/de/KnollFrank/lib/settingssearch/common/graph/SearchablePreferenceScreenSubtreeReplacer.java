package de.KnollFrank.lib.settingssearch.common.graph;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

public class SearchablePreferenceScreenSubtreeReplacer {

    // FK-TODO: refactor to Step-Builder (= Fluent Interface or Internal DSL)
    public static Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> replaceSubtreeWithTree(
            final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> originalGraph,
            final SearchablePreferenceScreen rootNodeOfSubtreeToReplace,
            final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> replacementTree) {
        return createSubtreeReplacer().replaceSubtreeWithTree(originalGraph, rootNodeOfSubtreeToReplace, replacementTree);
    }

    private static SubtreeReplacer<SearchablePreferenceScreen, SearchablePreferenceEdge> createSubtreeReplacer() {
        return new SubtreeReplacer<>(
                () -> new DefaultDirectedGraph<>(SearchablePreferenceEdge.class),
                edge -> new SearchablePreferenceEdge(edge.preference));
    }
}
