package de.KnollFrank.lib.settingssearch.common.graph;

import org.jgrapht.Graph;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.common.Sets;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

class PredecessorOfPreferencesOfNodeSetter {

    public static void setPredecessorOfPreferencesOfNode(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph,
                                                         final SearchablePreferenceScreen node) {
        final Optional<SearchablePreference> predecessor =
                PredecessorOfPreferencesOfNodeSetter
                        .getIncomingEdgeOfNode(graph, node)
                        .map(edge -> edge.preference);
        for (final SearchablePreference searchablePreference : node.allPreferencesOfPreferenceHierarchy()) {
            searchablePreference.setPredecessor(predecessor);
        }
    }

    private static Optional<SearchablePreferenceEdge> getIncomingEdgeOfNode(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph,
                                                                            final SearchablePreferenceScreen node) {
        return Sets.getOnlyElement(graph.incomingEdgesOf(node));
    }
}
