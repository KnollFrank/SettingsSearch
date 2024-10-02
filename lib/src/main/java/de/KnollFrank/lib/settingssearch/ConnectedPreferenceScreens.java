package de.KnollFrank.lib.settingssearch;

import androidx.preference.Preference;

import org.jgrapht.Graph;

import java.util.Map;
import java.util.Set;

public class ConnectedPreferenceScreens {

    private final Graph<PreferenceScreenWithHost, PreferenceEdge> searchablePreferenceScreenGraph;
    public final Map<Preference, PreferencePath> preferencePathByPreference;

    public ConnectedPreferenceScreens(final Graph<PreferenceScreenWithHost, PreferenceEdge> searchablePreferenceScreenGraph) {
        this.searchablePreferenceScreenGraph = searchablePreferenceScreenGraph;
        this.preferencePathByPreference = PreferencePathByPreferenceProvider.getPreferencePathByPreference(searchablePreferenceScreenGraph);
    }

    public Set<PreferenceScreenWithHost> getConnectedPreferenceScreens() {
        return searchablePreferenceScreenGraph.vertexSet();
    }
}
