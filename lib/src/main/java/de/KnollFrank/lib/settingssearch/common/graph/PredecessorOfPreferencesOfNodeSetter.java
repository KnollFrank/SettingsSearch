package de.KnollFrank.lib.settingssearch.common.graph;

import org.jgrapht.Graph;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

// FK-TODO: remove class
public class PredecessorOfPreferencesOfNodeSetter {

    public static void setPredecessorOfPreferencesOfNode(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph,
                                                         final SearchablePreferenceScreen node) {
    }

    private static Optional<SearchablePreference> getPredecessorOfPreferencesOfNode(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph, final SearchablePreferenceScreen node) {
        return getIncomingPreferenceOfNode(graph, node);
    }

    private static Optional<SearchablePreference> getIncomingPreferenceOfNode(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph,
                                                                              final SearchablePreferenceScreen node) {
        return Graphs
                .getIncomingEdgeOfNode(graph, node)
                .map(incomingEdgeOfNode -> incomingEdgeOfNode.preference);
    }
}
