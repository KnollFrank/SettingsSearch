package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenTestFactory.createScreen;

public class SearchablePreferenceWithinGraphTestFactory {

    public static SearchablePreferenceWithinGraph createSearchablePreferenceWithinGraph(final SearchablePreference preference) {
        return new SearchablePreferenceWithinGraph(
                preference,
                SearchablePreferenceScreenGraphTestFactory
                        .createGraphBuilder()
                        .addVertex(createScreen(preference))
                        .build());
    }
}
