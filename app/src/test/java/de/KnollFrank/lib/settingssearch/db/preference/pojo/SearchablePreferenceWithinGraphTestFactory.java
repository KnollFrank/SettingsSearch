package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenTestFactory.createScreen;

import de.KnollFrank.lib.settingssearch.common.graph.Tree;

@SuppressWarnings({"UnstableApiUsage"})
public class SearchablePreferenceWithinGraphTestFactory {

    public static SearchablePreferenceOfHostWithinTree createSearchablePreferenceWithinGraph(final SearchablePreference preference) {
        final SearchablePreferenceScreen screen = createScreen(preference);
        return new SearchablePreferenceOfHostWithinTree(
                preference,
                screen,
                new Tree<>(
                        SearchablePreferenceScreenGraphTestFactory
                                .createGraphBuilder()
                                .addNode(screen)
                                .build()));
    }
}
