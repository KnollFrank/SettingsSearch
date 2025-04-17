package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import org.jgrapht.Graph;

import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.PreferencePathByPojoPreferenceProvider;
import de.KnollFrank.lib.settingssearch.graph.HostClassFromPojoNodesRemover;

public class MergedPreferenceScreenDataFactory {

    public static Set<SearchablePreference> getPreferences(
            final Graph<PreferenceScreenWithHostClass, SearchablePreferenceEdge> pojoGraph) {
        final PreferencePathsSetter preferencePathsSetter =
                new PreferencePathsSetter(
                        PreferencePathByPojoPreferenceProvider.getPreferencePathByPojoPreference(
                                HostClassFromPojoNodesRemover.removeHostClassFromNodes(pojoGraph)));
        final Set<SearchablePreference> preferences = getPreferences(pojoGraph.vertexSet());
        preferencePathsSetter.setPreferencePaths(preferences);
        return preferences;
    }

    private static Set<SearchablePreference> getPreferences(final Set<PreferenceScreenWithHostClass> preferenceScreens) {
        return preferenceScreens
                .stream()
                .flatMap(preferenceScreenWithHostClass -> preferenceScreenWithHostClass.preferenceScreen().preferences().stream())
                .collect(Collectors.toSet());
    }
}
