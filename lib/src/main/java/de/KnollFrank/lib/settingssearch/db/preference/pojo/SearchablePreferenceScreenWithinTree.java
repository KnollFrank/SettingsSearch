package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.common.graph.TreePath;
import de.KnollFrank.lib.settingssearch.common.graph.Trees;

public record SearchablePreferenceScreenWithinTree(
        SearchablePreferenceScreen searchablePreferenceScreen,
        Tree<SearchablePreferenceScreen, SearchablePreference> treeContainingScreen) {

    public TreePath<SearchablePreferenceScreen, SearchablePreference> getTreePath() {
        return Trees.getPathFromRootNodeToTarget(treeContainingScreen, searchablePreferenceScreen);
    }

    public Set<SearchablePreferenceOfHostWithinTree> getAllPreferencesOfPreferenceHierarchy() {
        return searchablePreferenceScreen
                .allPreferencesOfPreferenceHierarchy()
                .stream()
                .map(searchablePreference ->
                             new SearchablePreferenceOfHostWithinTree(
                                     searchablePreference,
                                     searchablePreferenceScreen,
                                     treeContainingScreen))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final SearchablePreferenceScreenWithinTree that = (SearchablePreferenceScreenWithinTree) o;
        return Objects.equals(searchablePreferenceScreen, that.searchablePreferenceScreen);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(searchablePreferenceScreen);
    }
}
