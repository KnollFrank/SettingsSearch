package de.KnollFrank.lib.settingssearch.graph;

import org.jgrapht.Graph;

import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Sets;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

public class PojoGraphs {

    public static Set<SearchablePreference> getPreferences(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> pojoGraph) {
        return getPreferences(pojoGraph.vertexSet());
    }

    public static Set<SearchablePreference> getPreferences(final Set<SearchablePreferenceScreen> preferenceScreens) {
        return Sets.union(
                preferenceScreens
                        .stream()
                        .map(SearchablePreferenceScreen::getAllPreferences)
                        .collect(Collectors.toSet()));
    }
}
