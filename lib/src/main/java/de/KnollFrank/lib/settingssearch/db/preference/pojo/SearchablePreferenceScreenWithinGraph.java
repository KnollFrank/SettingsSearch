package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.graph.Graphs;

public record SearchablePreferenceScreenWithinGraph(
        SearchablePreferenceScreen searchablePreferenceScreen,
        Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graphContainingScreen) {

    public GraphPath<SearchablePreferenceScreen, SearchablePreferenceEdge> getGraphPath() {
        return Graphs.getPathFromRootNodeToTarget(graphContainingScreen, searchablePreferenceScreen);
    }

    public Set<SearchablePreferenceWithinGraph> getAllPreferencesOfPreferenceHierarchy() {
        return searchablePreferenceScreen
                .allPreferencesOfPreferenceHierarchy()
                .stream()
                .map(searchablePreference ->
                             new SearchablePreferenceWithinGraph(
                                     searchablePreference,
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
