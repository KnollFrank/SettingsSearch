package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenTestFactory.createScreen;

public class SearchablePreferenceWithinGraphTestFactory {

    public static SearchablePreferenceOfHostWithinGraph createSearchablePreferenceWithinGraph(final SearchablePreference preference) {
        final SearchablePreferenceScreen screen = createScreen(preference);
        return new SearchablePreferenceOfHostWithinGraph(
                preference,
                screen,
                SearchablePreferenceScreenGraphTestFactory
                        .createGraphBuilder()
                        .addVertex(screen)
                        .build());
    }
}
