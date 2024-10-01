package de.KnollFrank.lib.settingssearch.provider;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.SearchablePreferenceScreenWithHost;

@FunctionalInterface
public interface PreferenceScreenGraphAvailableListener {

	void onPreferenceScreenGraphWithoutInvisibleAndNonSearchablePreferencesAvailable(Graph<SearchablePreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph);
}
