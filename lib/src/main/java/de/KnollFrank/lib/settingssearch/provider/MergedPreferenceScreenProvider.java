package de.KnollFrank.lib.settingssearch.provider;

import androidx.preference.PreferenceManager;

import com.google.common.collect.BiMap;

import org.jgrapht.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.MergedPreferenceScreen;
import de.KnollFrank.lib.settingssearch.common.PreferencePOJOs;
import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJOEdge;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactoryAndInitializer;
import de.KnollFrank.lib.settingssearch.fragment.PreferencePathNavigator;

public class MergedPreferenceScreenProvider {

    private final boolean cacheMergedPreferenceScreens;
    private final FragmentFactoryAndInitializer fragmentFactoryAndInitializer;
    private final PreferenceManager preferenceManager;

    private static MergedPreferenceScreen mergedPreferenceScreen;

    public MergedPreferenceScreenProvider(final boolean cacheMergedPreferenceScreens,
                                          final FragmentFactoryAndInitializer fragmentFactoryAndInitializer,
                                          final PreferenceManager preferenceManager) {
        this.cacheMergedPreferenceScreens = cacheMergedPreferenceScreens;
        this.fragmentFactoryAndInitializer = fragmentFactoryAndInitializer;
        this.preferenceManager = preferenceManager;
    }

    public MergedPreferenceScreen getMergedPreferenceScreen(final Graph<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> pojoGraph) {
        if (!cacheMergedPreferenceScreens) {
            return computeMergedPreferenceScreen(pojoGraph);
        }
        if (mergedPreferenceScreen == null) {
            mergedPreferenceScreen = computeMergedPreferenceScreen(pojoGraph);
        }
        return mergedPreferenceScreen;
    }

    private MergedPreferenceScreen computeMergedPreferenceScreen(
            final Graph<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> pojoGraph) {
        return new MergedPreferenceScreen(
                () ->
                        PreferenceScreensMerger.mergePreferenceScreens(
                                new ArrayList<>(pojoGraph.vertexSet()),
                                preferenceManager),
                pojoEntityMap ->
                        PreferencePathByPreference.getPreferencePathByPreference(
                                pojoGraph,
                                pojoEntityMap),
                pojoEntityMap ->
                        getPreferencePathNavigator(
                                new ArrayList<>(pojoGraph.vertexSet()),
                                pojoEntityMap),
                PreferencePOJOs.getPreferencesRecursively(getPreferences(pojoGraph.vertexSet())));
    }

    private static Set<SearchablePreferencePOJO> getPreferences(final Set<PreferenceScreenWithHostClassPOJO> preferenceScreens) {
        return preferenceScreens
                .stream()
                .flatMap(preferenceScreenWithHostClassPOJO -> preferenceScreenWithHostClassPOJO.preferenceScreen().children().stream())
                .collect(Collectors.toSet());
    }

    private PreferencePathNavigator getPreferencePathNavigator(
            final List<PreferenceScreenWithHostClassPOJO> screens,
            final BiMap<SearchablePreferencePOJO, SearchablePreference> pojoEntityMap) {
        return new PreferencePathNavigator(
                HostByPreferenceProvider.getHostByPreference(screens),
                fragmentFactoryAndInitializer,
                pojoEntityMap,
                preferenceManager.getContext());
    }
}
