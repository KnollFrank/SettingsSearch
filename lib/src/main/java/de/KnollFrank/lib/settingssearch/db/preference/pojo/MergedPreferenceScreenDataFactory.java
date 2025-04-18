package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import org.jgrapht.Graph;

import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.PreferencePathByPojoPreferenceProvider;
import de.KnollFrank.lib.settingssearch.graph.HostClassFromPojoNodesRemover;

public class MergedPreferenceScreenDataFactory {

    public static Set<SearchablePreference> getPreferences(
            final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> pojoGraph) {
        final PreferencePathsSetter preferencePathsSetter =
                new PreferencePathsSetter(
                        PreferencePathByPojoPreferenceProvider.getPreferencePathByPojoPreference(
                                HostClassFromPojoNodesRemover.removeHostClassFromNodes(pojoGraph)));
        final Set<SearchablePreference> preferences = getPreferences(pojoGraph.vertexSet());
        preferencePathsSetter.setPreferencePaths(preferences);
        return preferences;
    }

    private static Set<SearchablePreference> getPreferences(final Set<SearchablePreferenceScreen> preferenceScreens) {
        return preferenceScreens
                .stream()
                .flatMap(preferenceScreen -> preferenceScreen.preferences().stream())
                .collect(Collectors.toSet());
    }
}
