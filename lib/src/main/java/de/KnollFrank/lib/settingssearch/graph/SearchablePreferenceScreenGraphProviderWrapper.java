package de.KnollFrank.lib.settingssearch.graph;

import android.content.Context;

@FunctionalInterface
public interface SearchablePreferenceScreenGraphProviderWrapper {

    SearchablePreferenceScreenGraphProvider wrap(SearchablePreferenceScreenGraphProvider searchablePreferenceScreenGraphProvider,
                                                 Context context);
}
