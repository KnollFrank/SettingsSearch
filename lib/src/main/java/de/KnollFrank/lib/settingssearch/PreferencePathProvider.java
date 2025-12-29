package de.KnollFrank.lib.settingssearch;

import com.google.common.collect.ImmutableList;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

import java.util.List;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.graph.Graphs;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

public class PreferencePathProvider {

    public static PreferencePath getPreferencePath(
            final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph,
            final SearchablePreference target) {
        return new PreferencePath(getPathPreferences(graph, target));
    }

    private static List<SearchablePreference> getPathPreferences(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph,
                                                                 final SearchablePreference target) {
        return ImmutableList
                .<SearchablePreference>builder()
                .addAll(getPathPreferences(graph, target.getHost()))
                .add(target)
                .build();
    }

    private static List<SearchablePreference> getPathPreferences(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph,
                                                                 final SearchablePreferenceScreen target) {
        return getEdgePreferences(Graphs.getPathFromRootNodeToTarget(graph, target));
    }

    private static List<SearchablePreference> getEdgePreferences(final GraphPath<SearchablePreferenceScreen, SearchablePreferenceEdge> graphPath) {
        return graphPath
                .getEdgeList()
                .stream()
                .map(searchablePreferenceEdge -> searchablePreferenceEdge.preference)
                .collect(Collectors.toList());
    }
}
