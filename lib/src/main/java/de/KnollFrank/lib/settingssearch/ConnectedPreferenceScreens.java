package de.KnollFrank.lib.settingssearch;

import androidx.preference.Preference;

import org.jgrapht.Graph;

import java.util.Map;
import java.util.Set;

public class ConnectedPreferenceScreens {

	public final Graph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph;
	// FK-TODO: replace with preferenceScreenGraph.vertexSet() ?
	public final Set<PreferenceScreenWithHost> connectedPreferenceScreens;
	// FK-TODO: replace with PreferencePathByPreferenceProvider.getPreferencePathByPreference(preferenceScreenGraph) ?
	public final Map<Preference, PreferencePath> preferencePathByPreference;

	public ConnectedPreferenceScreens(final Graph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph,
									  final Set<PreferenceScreenWithHost> connectedPreferenceScreens,
									  final Map<Preference, PreferencePath> preferencePathByPreference) {
		this.preferenceScreenGraph = preferenceScreenGraph;
		this.connectedPreferenceScreens = connectedPreferenceScreens;
		this.preferencePathByPreference = preferencePathByPreference;
	}
}
