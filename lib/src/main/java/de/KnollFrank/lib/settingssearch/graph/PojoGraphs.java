package de.KnollFrank.lib.settingssearch.graph;

import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Sets;
import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceOfHostWithinGraph;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenWithinGraph;

@SuppressWarnings({"UnstableApiUsage"})
public class PojoGraphs {

    public static Set<SearchablePreferenceOfHostWithinGraph> getPreferences(final Tree<SearchablePreferenceScreen, SearchablePreference> pojoGraph) {
        return getPreferences(
                pojoGraph
                        .graph()
                        .nodes()
                        .stream()
                        .map(searchablePreferenceScreen ->
                                     new SearchablePreferenceScreenWithinGraph(
                                             searchablePreferenceScreen,
                                             pojoGraph))
                        .collect(Collectors.toSet()));
    }

    public static Set<SearchablePreferenceOfHostWithinGraph> getPreferences(final Set<SearchablePreferenceScreenWithinGraph> preferenceScreens) {
        return Sets.union(
                preferenceScreens
                        .stream()
                        .map(SearchablePreferenceScreenWithinGraph::getAllPreferencesOfPreferenceHierarchy)
                        .collect(Collectors.toSet()));
    }
}
