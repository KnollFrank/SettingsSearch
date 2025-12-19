package de.KnollFrank.lib.settingssearch.common.graph;

import org.jgrapht.Graph;

import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.common.Sets;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

class PredecessorOfPreferencesOfNodeSetter {

    public static void setPredecessorOfPreferencesOfNode(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph,
                                                         final SearchablePreferenceScreen node) {
        setPredecessor(node.allPreferencesOfPreferenceHierarchy(), getPredecessorOfPreferencesOfNode(graph, node));
    }

    private static void setPredecessor(final Set<SearchablePreference> preferences, final Optional<SearchablePreference> predecessor) {
        preferences.forEach(preference -> preference.setPredecessor(predecessor));
    }

    private static Optional<SearchablePreference> getPredecessorOfPreferencesOfNode(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph, final SearchablePreferenceScreen node) {
        return getIncomingPreferenceOfNode(graph, node);
    }

    private static Optional<SearchablePreference> getIncomingPreferenceOfNode(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph,
                                                                              final SearchablePreferenceScreen node) {
        return PredecessorOfPreferencesOfNodeSetter
                .getIncomingEdgeOfNode(graph, node)
                .map(incomingEdgeOfNode -> incomingEdgeOfNode.preference);
    }

    private static Optional<SearchablePreferenceEdge> getIncomingEdgeOfNode(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph,
                                                                            final SearchablePreferenceScreen node) {
        return Sets.asOptional(graph.incomingEdgesOf(node));
    }
}
