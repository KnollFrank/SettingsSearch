package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.collect.Iterables;

import org.jgrapht.Graph;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

class PredecessorOfPreferencesOfNodeSetter {

    public static void setPredecessorOfPreferencesOfNode(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph,
                                                         final SearchablePreferenceScreen node) {
        final SearchablePreference predecessor =
                PredecessorOfPreferencesOfNodeSetter
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
