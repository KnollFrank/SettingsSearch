package de.KnollFrank.lib.settingssearch;

import com.google.common.collect.ImmutableList;

import org.jgrapht.GraphPath;

import java.util.List;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceOfHostWithinGraph;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenWithinGraph;

// FK-TODO: refactor
public class PreferencePathProvider {

    public static PreferencePath getPreferencePath(final SearchablePreferenceOfHostWithinGraph target) {
        return new PreferencePath(getPathPreferences(target));
    }

    private static List<SearchablePreferenceOfHostWithinGraph> getPathPreferences(final SearchablePreferenceOfHostWithinGraph target) {
        return ImmutableList
                .<SearchablePreferenceOfHostWithinGraph>builder()
                .addAll(
                        getPathPreferences(
                                new SearchablePreferenceScreenWithinGraph(
                                        target.hostOfPreference(),
                                        target.graphContainingPreference())))
                .add(target)
                .build();
    }

    private static List<SearchablePreferenceOfHostWithinGraph> getPathPreferences(final SearchablePreferenceScreenWithinGraph target) {
        return getEdgePreferences(target.getGraphPath());
    }

    private static List<SearchablePreferenceOfHostWithinGraph> getEdgePreferences(final GraphPath<SearchablePreferenceScreen, SearchablePreferenceEdge> graphPath) {
        return graphPath
                .getEdgeList()
                .stream()
                .map(searchablePreferenceEdge ->
                             new SearchablePreferenceOfHostWithinGraph(
                                     searchablePreferenceEdge.preference,
                                     graphPath.getGraph().getEdgeSource(searchablePreferenceEdge),
                                     graphPath.getGraph()))
                .collect(Collectors.toList());
    }
}
