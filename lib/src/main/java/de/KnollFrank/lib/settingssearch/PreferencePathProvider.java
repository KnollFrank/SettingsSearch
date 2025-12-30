package de.KnollFrank.lib.settingssearch;

import com.google.common.collect.ImmutableList;

import org.jgrapht.GraphPath;

import java.util.List;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenWithinGraph;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceWithinGraph;

// FK-TODO: refactor
public class PreferencePathProvider {

    public static PreferencePath getPreferencePath(final SearchablePreferenceWithinGraph target) {
        return new PreferencePath(getPathPreferences(target));
    }

    private static List<SearchablePreferenceWithinGraph> getPathPreferences(final SearchablePreferenceWithinGraph target) {
        return ImmutableList
                .<SearchablePreferenceWithinGraph>builder()
                .addAll(
                        getPathPreferences(
                                new SearchablePreferenceScreenWithinGraph(
                                        target.searchablePreference().getHost(),
                                        target.graphContainingPreference())))
                .add(target)
                .build();
    }

    private static List<SearchablePreferenceWithinGraph> getPathPreferences(final SearchablePreferenceScreenWithinGraph target) {
        return getEdgePreferences(target.getGraphPath());
    }

    // FK-TODO: man kann aus einem GraphPath<SearchablePreferenceScreen, SearchablePreferenceEdge> den PreferencePath berechnen. Vielleicht überall mit dem GraphPath<SearchablePreferenceScreen, SearchablePreferenceEdge> arbeiten?
    private static List<SearchablePreferenceWithinGraph> getEdgePreferences(final GraphPath<SearchablePreferenceScreen, SearchablePreferenceEdge> graphPath) {
        return graphPath
                .getEdgeList()
                .stream()
                .map(searchablePreferenceEdge ->
                             new SearchablePreferenceWithinGraph(
                                     searchablePreferenceEdge.preference,
                                     graphPath.getGraph()))
                .collect(Collectors.toList());
    }
}
