package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import org.jgrapht.Graph;

import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Sets;

public class MergedPreferenceScreenDataFactory {

    public static Set<SearchablePreference> getPreferences(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> pojoGraph) {
        return getPreferences(pojoGraph.vertexSet());
    }

    public static Set<SearchablePreference> getPreferences(final Set<SearchablePreferenceScreen> preferenceScreens) {
        return Sets.union(
                preferenceScreens
                        .stream()
                        .map(SearchablePreferenceScreen::allPreferences)
                        .collect(Collectors.toSet()));
    }
}
