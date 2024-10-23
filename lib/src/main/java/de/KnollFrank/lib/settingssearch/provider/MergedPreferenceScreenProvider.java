package de.KnollFrank.lib.settingssearch.provider;

import android.content.Context;

import androidx.preference.PreferenceManager;

import com.google.common.collect.BiMap;

import org.jgrapht.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.MergedPreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceScreenFromPOJOConverter.PreferenceScreenWithMap;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJOEdge;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactoryAndInitializer;
import de.KnollFrank.lib.settingssearch.fragment.PreferencePathNavigator;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoAttribute;

public class MergedPreferenceScreenProvider {

    private final SearchableInfoAttribute searchableInfoAttribute;
    private final boolean cacheMergedPreferenceScreens;
    private final FragmentFactoryAndInitializer fragmentFactoryAndInitializer;
    // FK-TODO: remove context and replace with preferenceManager.getContext()
    private final Context context;
    private final PreferenceManager preferenceManager;

    private static MergedPreferenceScreen mergedPreferenceScreen;

    public MergedPreferenceScreenProvider(final SearchableInfoAttribute searchableInfoAttribute,
                                          final boolean cacheMergedPreferenceScreens,
                                          final FragmentFactoryAndInitializer fragmentFactoryAndInitializer,
                                          final Context context,
                                          final PreferenceManager preferenceManager) {
        this.searchableInfoAttribute = searchableInfoAttribute;
        this.cacheMergedPreferenceScreens = cacheMergedPreferenceScreens;
        this.fragmentFactoryAndInitializer = fragmentFactoryAndInitializer;
        this.context = context;
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
        final PreferenceScreenWithMap preferenceScreenWithMap =
                PreferenceScreensMerger.mergePreferenceScreens(
                        new ArrayList<>(pojoGraph.vertexSet()),
                        preferenceManager);
        return new MergedPreferenceScreen(
                preferenceScreenWithMap.preferenceScreen(),
                preferenceScreenWithMap.pojoEntityMap(),
                Set.of(),
                PreferencePathByPreference.getPreferencePathByPreference(
                        pojoGraph,
                        preferenceScreenWithMap.pojoEntityMap()),
                searchableInfoAttribute,
                getPreferencePathNavigator(
                        new ArrayList<>(pojoGraph.vertexSet()),
                        preferenceScreenWithMap.pojoEntityMap()));
    }

    private PreferencePathNavigator getPreferencePathNavigator(
            final List<PreferenceScreenWithHostClassPOJO> screens,
            final BiMap<SearchablePreferencePOJO, SearchablePreference> pojoEntityMap) {
        return new PreferencePathNavigator(
                HostByPreferenceProvider.getHostByPreference(screens),
                fragmentFactoryAndInitializer,
                pojoEntityMap,
                context);
    }
}
