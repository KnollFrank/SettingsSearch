package de.KnollFrank.lib.settingssearch.graph;

import org.jgrapht.Graph;

import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Sets;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen.DbDataProvider;

public class PojoGraphs {

    public static Set<SearchablePreference> getPreferences(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> pojoGraph,
                                                           final DbDataProvider dbDataProvider) {
        return getPreferences(pojoGraph.vertexSet(), dbDataProvider);
    }

    public static Set<SearchablePreference> getPreferences(final Set<SearchablePreferenceScreen> preferenceScreens,
                                                           final DbDataProvider dbDataProvider) {
        return Sets.union(
                preferenceScreens
                        .stream()
                        .map(searchablePreferenceScreen -> searchablePreferenceScreen.getAllPreferences(dbDataProvider))
                        .collect(Collectors.toSet()));
    }
}
