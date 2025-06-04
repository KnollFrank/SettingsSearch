package de.KnollFrank.lib.settingssearch.graph;

import org.jgrapht.Graph;

import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Sets;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity.DbDataProvider;

public class PojoGraphs {

    public static Set<SearchablePreferenceEntity> getPreferences(final Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEdge> pojoGraph,
                                                                 final DbDataProvider dbDataProvider) {
        return getPreferences(pojoGraph.vertexSet(), dbDataProvider);
    }

    public static Set<SearchablePreferenceEntity> getPreferences(final Set<SearchablePreferenceScreenEntity> preferenceScreens,
                                                                 final DbDataProvider dbDataProvider) {
        return Sets.union(
                preferenceScreens
                        .stream()
                        .map(searchablePreferenceScreen -> searchablePreferenceScreen.getAllPreferences(dbDataProvider))
                        .collect(Collectors.toSet()));
    }
}
