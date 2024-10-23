package de.KnollFrank.lib.settingssearch.search;

import androidx.preference.PreferenceManager;

import de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenGraphProvider;

@FunctionalInterface
public interface SearchablePreferenceScreenGraphProviderWrapper {

    SearchablePreferenceScreenGraphProvider wrap(SearchablePreferenceScreenGraphProvider searchablePreferenceScreenGraphProvider,
                                                 PreferenceManager preferenceManager);
}
