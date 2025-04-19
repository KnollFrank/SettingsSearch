package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import org.jgrapht.Graph;

import java.util.Set;
import java.util.stream.Collectors;

public class MergedPreferenceScreenDataFactory {

    public static Set<SearchablePreference> getPreferences(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> pojoGraph) {
        return getPreferences(pojoGraph.vertexSet());
    }

    public static Set<SearchablePreference> getPreferences(final Set<SearchablePreferenceScreen> preferenceScreens) {
        return preferenceScreens
                .stream()
                .flatMap(preferenceScreen -> preferenceScreen.preferences().stream())
                .collect(Collectors.toSet());
    }
}
