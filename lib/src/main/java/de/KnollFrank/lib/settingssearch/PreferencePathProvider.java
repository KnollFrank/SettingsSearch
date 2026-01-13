package de.KnollFrank.lib.settingssearch;

import com.google.common.collect.ImmutableList;
import com.google.common.graph.ImmutableValueGraph;

import java.util.List;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.graph.TreePath;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceOfHostWithinTree;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenWithinTree;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class PreferencePathProvider {

    public static PreferencePath getPreferencePath(final SearchablePreferenceOfHostWithinTree target) {
        return new PreferencePath(getPathPreferences(target));
    }

    private static List<SearchablePreferenceOfHostWithinTree> getPathPreferences(final SearchablePreferenceOfHostWithinTree target) {
        return ImmutableList
                .<SearchablePreferenceOfHostWithinTree>builder()
                .addAll(
                        getPathPreferences(
                                new SearchablePreferenceScreenWithinTree(
                                        target.hostOfPreference(),
                                        target.treeContainingPreference())))
                .add(target)
                .build();
    }

    private static List<SearchablePreferenceOfHostWithinTree> getPathPreferences(final SearchablePreferenceScreenWithinTree target) {
        return getEdgePreferences(target.getTreePath());
    }

    private static List<SearchablePreferenceOfHostWithinTree> getEdgePreferences(final TreePath<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> treePath) {
        return treePath
                .edges()
                .stream()
                .map(searchablePreferenceEdge ->
                             new SearchablePreferenceOfHostWithinTree(
                                     searchablePreferenceEdge.edgeValue(),
                                     searchablePreferenceEdge.edge().source(),
                                     treePath.tree()))
                .collect(Collectors.toList());
    }
}
