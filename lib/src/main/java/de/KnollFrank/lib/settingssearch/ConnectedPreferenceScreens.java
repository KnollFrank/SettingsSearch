package de.KnollFrank.lib.settingssearch;

import androidx.preference.Preference;

import org.jgrapht.Graph;

import java.util.Map;
import java.util.Set;

public class ConnectedPreferenceScreens {

    public final Graph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph;
    public final Map<Preference, PreferencePath> preferencePathByPreference;

    public ConnectedPreferenceScreens(final Graph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph) {
        this.preferenceScreenGraph = preferenceScreenGraph;
        this.preferencePathByPreference = PreferencePathByPreferenceProvider.getPreferencePathByPreference(preferenceScreenGraph);
    }

    public Set<PreferenceScreenWithHost> getConnectedPreferenceScreens() {
        return preferenceScreenGraph.vertexSet();
    }
}
