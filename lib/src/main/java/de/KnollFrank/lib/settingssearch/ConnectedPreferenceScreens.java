package de.KnollFrank.lib.settingssearch;

import androidx.preference.Preference;

import org.jgrapht.Graph;

import java.util.Map;
import java.util.Set;

public record ConnectedPreferenceScreens(
        Set<PreferenceScreenWithHost> connectedPreferenceScreens,
        Map<Preference, PreferencePath> preferencePathByPreference) {

    public static ConnectedPreferenceScreens fromSearchablePreferenceScreenGraph(final Graph<PreferenceScreenWithHost, PreferenceEdge> searchablePreferenceScreenGraph) {
        return new ConnectedPreferenceScreens(
                searchablePreferenceScreenGraph.vertexSet(),
                PreferencePathByPreferenceProvider.getPreferencePathByPreference(searchablePreferenceScreenGraph));
    }
}
