package de.KnollFrank.lib.settingssearch.provider;

import android.content.Context;

import androidx.preference.PreferenceManager;

import com.google.common.collect.HashBiMap;

import org.jgrapht.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.MergedPreferenceScreen;
import de.KnollFrank.lib.settingssearch.common.PreferencePOJOs;
import de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceScreenFromPOJOConverter.PreferenceScreenWithMap;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJOEdge;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactoryAndInitializer;
import de.KnollFrank.lib.settingssearch.fragment.PreferencePathNavigator;
import de.KnollFrank.lib.settingssearch.results.SearchResultsPreferenceScreenHelper;

public class MergedPreferenceScreenProvider {

    private final boolean cacheMergedPreferenceScreens;
    private final FragmentFactoryAndInitializer fragmentFactoryAndInitializer;
    private final PreferenceManager preferenceManager;
    private final Context context;

    private static MergedPreferenceScreen mergedPreferenceScreen;

    public MergedPreferenceScreenProvider(final boolean cacheMergedPreferenceScreens,
                                          final FragmentFactoryAndInitializer fragmentFactoryAndInitializer,
                                          final PreferenceManager preferenceManager,
                                          final Context context) {
        this.cacheMergedPreferenceScreens = cacheMergedPreferenceScreens;
        this.fragmentFactoryAndInitializer = fragmentFactoryAndInitializer;
        this.preferenceManager = preferenceManager;
        this.context = context;
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
                PreferencePOJOs.getPreferencesRecursively(getPreferences(pojoGraph.vertexSet())),
                new SearchResultsPreferenceScreenHelper(
                        () ->
                                new PreferenceScreenWithMap(
                                        preferenceManager.createPreferenceScreen(preferenceManager.getContext()),
                                        HashBiMap.create()),
                        getPreferencePathNavigator(new ArrayList<>(pojoGraph.vertexSet())),
                        pojoEntityMap ->
                                PreferencePathByPreference.getPreferencePathByPreference(
                                        pojoGraph,
                                        pojoEntityMap),
                        context));
    }

    private static Set<SearchablePreferencePOJO> getPreferences(final Set<PreferenceScreenWithHostClassPOJO> preferenceScreens) {
        return preferenceScreens
                .stream()
                .flatMap(preferenceScreenWithHostClassPOJO -> preferenceScreenWithHostClassPOJO.preferenceScreen().children().stream())
                .collect(Collectors.toSet());
    }

    private PreferencePathNavigator getPreferencePathNavigator(final List<PreferenceScreenWithHostClassPOJO> screens) {
        return new PreferencePathNavigator(
                HostByPreferenceProvider.getHostByPreference(screens),
                fragmentFactoryAndInitializer,
                preferenceManager.getContext());
    }
}
