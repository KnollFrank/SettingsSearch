package de.KnollFrank.lib.settingssearch.provider;

import androidx.preference.Preference;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.common.graph.Tree;

@FunctionalInterface
public interface PreferenceScreenTreeAvailableListener {

	void onPreferenceScreenTreeAvailable(Tree<PreferenceScreenWithHost, Preference> preferenceScreenTree);
}
