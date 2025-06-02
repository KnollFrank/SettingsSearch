package de.KnollFrank.lib.settingssearch.graph;

import org.jgrapht.Graph;

import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Sets;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntityEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;

public class PojoGraphs {

    public static Set<SearchablePreferenceEntity> getPreferences(final Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> pojoGraph) {
        return getPreferences(pojoGraph.vertexSet());
    }

    public static Set<SearchablePreferenceEntity> getPreferences(final Set<SearchablePreferenceScreenEntity> preferenceScreens) {
        return Sets.union(
                preferenceScreens
                        .stream()
                        .map(SearchablePreferenceScreenEntity::getAllPreferences)
                        .collect(Collectors.toSet()));
    }
}
