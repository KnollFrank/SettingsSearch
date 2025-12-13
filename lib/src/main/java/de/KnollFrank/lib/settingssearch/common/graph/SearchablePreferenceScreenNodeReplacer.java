package de.KnollFrank.lib.settingssearch.common.graph;

import static de.KnollFrank.lib.settingssearch.common.graph.PredecessorOfPreferencesOfNodeSetter.setPredecessorOfPreferencesOfNode;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

public class SearchablePreferenceScreenNodeReplacer {

    public static Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> replaceNode(
            final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> originalGraph,
            final SearchablePreferenceScreen nodeOfOriginalGraphToReplace,
            final SearchablePreferenceScreen replacementNode) {
        final var resultGraph = createNodeReplacer().replaceNode(originalGraph, nodeOfOriginalGraphToReplace, replacementNode);
        setPredecessorOfPreferencesOfNode(resultGraph, replacementNode);
        return resultGraph;
    }

    private static NodeReplacer<SearchablePreferenceScreen, SearchablePreferenceEdge> createNodeReplacer() {
        return new NodeReplacer<>(
                SearchablePreferenceEdge.class,
                edge -> new SearchablePreferenceEdge(edge.preference));
    }
}
