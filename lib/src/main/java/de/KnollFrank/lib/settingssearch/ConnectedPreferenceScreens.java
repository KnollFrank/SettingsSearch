package de.KnollFrank.lib.settingssearch;

import androidx.preference.Preference;

import org.jgrapht.Graph;

import java.util.Map;
import java.util.Set;

public class ConnectedPreferenceScreens {

	public final Graph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph;
    // FK-TODO: replace with PreferencePathByPreferenceProvider.getPreferencePathByPreference(preferenceScreenGraph) ?
	public final Map<Preference, PreferencePath> preferencePathByPreference;

	public ConnectedPreferenceScreens(final Graph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph,
									  final Map<Preference, PreferencePath> preferencePathByPreference) {
		this.preferenceScreenGraph = preferenceScreenGraph;
        this.preferencePathByPreference = preferencePathByPreference;
	}

	public Set<PreferenceScreenWithHost> getConnectedPreferenceScreens() {
		return preferenceScreenGraph.vertexSet();
	}
}
