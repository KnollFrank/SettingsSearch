package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import org.jgrapht.Graph;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.PreferencePathByPojoPreferenceProvider;
import de.KnollFrank.lib.settingssearch.graph.HostClassFromPojoNodesRemover;
import de.KnollFrank.lib.settingssearch.provider.HostByPreferenceProvider;

public class MergedPreferenceScreenDataFactory {

    public static Set<SearchablePreference> getPreferences(
            final Graph<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> pojoGraph) {
        final PreferencePathsAndHostsSetter preferencePathsAndHostsSetter =
                new PreferencePathsAndHostsSetter(
                        PreferencePathByPojoPreferenceProvider.getPreferencePathByPojoPreference(
                                HostClassFromPojoNodesRemover.removeHostClassFromNodes(pojoGraph)),
                        HostByPreferenceProvider.getHostByPreference(new ArrayList<>(pojoGraph.vertexSet())));
        final Set<SearchablePreference> preferences = getPreferences(pojoGraph.vertexSet());
        preferencePathsAndHostsSetter.setPreferencePathsAndHosts(preferences);
        return preferences;
    }

    private static Set<SearchablePreference> getPreferences(final Set<PreferenceScreenWithHostClassPOJO> preferenceScreens) {
        return preferenceScreens
                .stream()
                .flatMap(preferenceScreenWithHostClassPOJO -> preferenceScreenWithHostClassPOJO.preferenceScreen().children().stream())
                .collect(Collectors.toSet());
    }
}
