package de.KnollFrank.lib.settingssearch.graph;

import de.KnollFrank.lib.settingssearch.ConnectedSearchablePreferenceScreens;

public class PreferenceScreensProvider {

    public static ConnectedSearchablePreferenceScreens getConnectedPreferenceScreens(
            final SearchablePreferenceScreenGraphProvider searchablePreferenceScreenGraphProvider) {
        return ConnectedSearchablePreferenceScreens.fromSearchablePreferenceScreenGraph(
                searchablePreferenceScreenGraphProvider.getSearchablePreferenceScreenGraph());
    }
}
