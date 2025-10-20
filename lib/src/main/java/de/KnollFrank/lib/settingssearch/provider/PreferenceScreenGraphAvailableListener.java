package de.KnollFrank.lib.settingssearch.provider;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;

@FunctionalInterface
public interface PreferenceScreenGraphAvailableListener {

	void onPreferenceScreenGraphAvailable(Graph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph);
}
