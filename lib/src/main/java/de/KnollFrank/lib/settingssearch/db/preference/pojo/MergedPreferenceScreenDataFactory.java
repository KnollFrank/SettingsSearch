package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import org.jgrapht.Graph;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.PreferencePathByPojoPreferenceProvider;
import de.KnollFrank.lib.settingssearch.common.PreferencePOJOs;
import de.KnollFrank.lib.settingssearch.graph.HostClassFromPojoNodesRemover;
import de.KnollFrank.lib.settingssearch.provider.HostByPreferenceProvider;

public class MergedPreferenceScreenDataFactory {

    public static MergedPreferenceScreenData getMergedPreferenceScreenData(
            final Graph<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> pojoGraph) {
        return new MergedPreferenceScreenData(
                PreferencePOJOs.getPreferencesRecursively(getPreferences(pojoGraph.vertexSet())),
                PreferencePathByPojoPreferenceProvider.getPreferencePathByPojoPreference(
                        HostClassFromPojoNodesRemover.removeHostClassFromNodes(pojoGraph)),
                HostByPreferenceProvider.getHostByPreference(new ArrayList<>(pojoGraph.vertexSet())));
    }

    private static Set<SearchablePreferencePOJO> getPreferences(final Set<PreferenceScreenWithHostClassPOJO> preferenceScreens) {
        return preferenceScreens
                .stream()
                .flatMap(preferenceScreenWithHostClassPOJO -> preferenceScreenWithHostClassPOJO.preferenceScreen().children().stream())
                .collect(Collectors.toSet());
    }
}