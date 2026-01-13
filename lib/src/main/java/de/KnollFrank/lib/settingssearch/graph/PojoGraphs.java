package de.KnollFrank.lib.settingssearch.graph;

import com.google.common.graph.ValueGraph;

import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Sets;
import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceOfHostWithinTree;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenWithinTree;

public class PojoGraphs {

    @SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
    public static Set<SearchablePreferenceOfHostWithinTree> getPreferences(final Tree<SearchablePreferenceScreen, SearchablePreference, ? extends ValueGraph<SearchablePreferenceScreen, SearchablePreference>> pojoGraph) {
        return getPreferences(
                pojoGraph
                        .graph()
                        .nodes()
                        .stream()
                        .map(searchablePreferenceScreen ->
                                new SearchablePreferenceScreenWithinTree(
                                        searchablePreferenceScreen,
                                        pojoGraph))
                        .collect(Collectors.toSet()));
    }

    public static Set<SearchablePreferenceOfHostWithinTree> getPreferences(final Set<SearchablePreferenceScreenWithinTree> preferenceScreens) {
        return Sets.union(
                preferenceScreens
                        .stream()
                        .map(SearchablePreferenceScreenWithinTree::getAllPreferencesOfPreferenceHierarchy)
                        .collect(Collectors.toSet()));
    }
}
