package de.KnollFrank.lib.settingssearch.common.graph;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

public class SearchablePreferenceScreenNodeReplacer {

    // FK-TODO: refactor to Step-Builder (= Fluent Interface or Internal DSL)
    public static Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> replaceNode(
            final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> originalGraph,
            final SearchablePreferenceScreen nodeOfOriginalGraphToReplace,
            final SearchablePreferenceScreen replacementNode) {
        return createNodeReplacer().replaceNode(originalGraph, nodeOfOriginalGraphToReplace, replacementNode);
    }

    private static NodeReplacer<SearchablePreferenceScreen, SearchablePreferenceEdge> createNodeReplacer() {
        return new NodeReplacer<>(
                SearchablePreferenceEdge.class,
                edge -> new SearchablePreferenceEdge(edge.preference));
    }
}
