package de.KnollFrank.lib.settingssearch;

import androidx.preference.Preference;

import org.jgrapht.Graph;

import java.util.Map;
import java.util.Set;

public record ConnectedSearchablePreferenceScreens(
        Set<PreferenceScreenWithHostClass> connectedSearchablePreferenceScreens,
        Map<Preference, PreferencePath> preferencePathByPreference) {

    public static ConnectedSearchablePreferenceScreens fromSearchablePreferenceScreenGraph(final Graph<PreferenceScreenWithHostClass, PreferenceEdge> searchablePreferenceScreenGraph) {
        return new ConnectedSearchablePreferenceScreens(
                searchablePreferenceScreenGraph.vertexSet(),
                PreferencePathByPreferenceProvider.getPreferencePathByPreference(searchablePreferenceScreenGraph));
    }
}
