package de.KnollFrank.lib.settingssearch;

import androidx.preference.Preference;

import org.jgrapht.Graph;

import java.util.Map;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.graph.HostClassFromNodesRemover;

public record ConnectedSearchablePreferenceScreens(
        Set<PreferenceScreenWithHostClass> connectedSearchablePreferenceScreens,
        Map<Preference, PreferencePath> preferencePathByPreference) {

    public static ConnectedSearchablePreferenceScreens fromSearchablePreferenceScreenGraph(final Graph<PreferenceScreenWithHostClass, PreferenceEdge> searchablePreferenceScreenGraph) {
        return new ConnectedSearchablePreferenceScreens(
                searchablePreferenceScreenGraph.vertexSet(),
                PreferencePathByPreferenceProvider.getPreferencePathByPreference(
                        HostClassFromNodesRemover.removeHostClassFromNodes(
                                searchablePreferenceScreenGraph)));
    }
}
