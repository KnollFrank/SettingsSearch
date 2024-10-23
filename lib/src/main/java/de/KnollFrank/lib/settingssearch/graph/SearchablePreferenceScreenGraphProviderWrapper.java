package de.KnollFrank.lib.settingssearch.graph;

import androidx.preference.PreferenceManager;

@FunctionalInterface
public interface SearchablePreferenceScreenGraphProviderWrapper {

    SearchablePreferenceScreenGraphProvider wrap(SearchablePreferenceScreenGraphProvider searchablePreferenceScreenGraphProvider,
                                                 PreferenceManager preferenceManager);
}
