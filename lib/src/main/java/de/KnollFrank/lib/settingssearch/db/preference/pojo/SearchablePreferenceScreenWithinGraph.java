package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import org.jgrapht.GraphPath;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.graph.Graphs;
import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.graph.GraphConverterFactory;

public record SearchablePreferenceScreenWithinGraph(
        SearchablePreferenceScreen searchablePreferenceScreen,
        Tree<SearchablePreferenceScreen, SearchablePreference> graphContainingScreen) {

    public GraphPath<SearchablePreferenceScreen, SearchablePreferenceEdge> getGraphPath() {
        return Graphs.getPathFromRootNodeToTarget(
                GraphConverterFactory
                        .createSearchablePreferenceScreenGraphConverter()
                        .toJGraphT(graphContainingScreen.graph()),
                searchablePreferenceScreen);
    }

    public Set<SearchablePreferenceOfHostWithinGraph> getAllPreferencesOfPreferenceHierarchy() {
        return searchablePreferenceScreen
                .allPreferencesOfPreferenceHierarchy()
                .stream()
                .map(searchablePreference ->
                             new SearchablePreferenceOfHostWithinGraph(
                                     searchablePreference,
                                     searchablePreferenceScreen,
                                     graphContainingScreen))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final SearchablePreferenceScreenWithinGraph that = (SearchablePreferenceScreenWithinGraph) o;
        return Objects.equals(searchablePreferenceScreen, that.searchablePreferenceScreen);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(searchablePreferenceScreen);
    }
}
