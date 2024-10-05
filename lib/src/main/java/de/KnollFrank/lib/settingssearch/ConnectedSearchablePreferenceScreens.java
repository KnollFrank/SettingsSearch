package de.KnollFrank.lib.settingssearch;

import androidx.preference.Preference;

import org.jgrapht.Graph;

import java.util.Map;
import java.util.Set;

// FK-TODO: diesen Record in einen POJO umwandeln, als JSON speichern und wieder zurückwandeln in einen POJO und dann in einen Record.
//          verwende dazu POJOConverter und SearchablePreferenceScreenPOJODAO
public record ConnectedSearchablePreferenceScreens(
        Set<PreferenceScreenWithHostClass> connectedSearchablePreferenceScreens,
        Map<Preference, PreferencePath> preferencePathByPreference) {

    public static ConnectedSearchablePreferenceScreens fromSearchablePreferenceScreenGraph(final Graph<PreferenceScreenWithHostClass, PreferenceEdge> searchablePreferenceScreenGraph) {
        return new ConnectedSearchablePreferenceScreens(
                searchablePreferenceScreenGraph.vertexSet(),
                PreferencePathByPreferenceProvider.getPreferencePathByPreference(searchablePreferenceScreenGraph));
    }
}
