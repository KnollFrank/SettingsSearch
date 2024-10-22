package de.KnollFrank.lib.settingssearch.provider;

import static de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClass2POJOConverter.PreferenceScreenWithHostClassPOJOWithMap;

import android.content.Context;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.google.common.collect.BiMap;

import org.jgrapht.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.MergedPreferenceScreen;
import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.PreferencePathByPreferenceProvider;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostClass;
import de.KnollFrank.lib.settingssearch.common.Lists;
import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceScreenFromPOJOConverter;
import de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceScreenFromPOJOConverter.PreferenceScreenWithMap;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJOEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenPOJO;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactoryAndInitializer;
import de.KnollFrank.lib.settingssearch.fragment.PreferencePathNavigator;
import de.KnollFrank.lib.settingssearch.graph.Graph2POJOGraphTransformer;
import de.KnollFrank.lib.settingssearch.graph.HostClassAndMapFromNodesRemover;
import de.KnollFrank.lib.settingssearch.graph.MapFromPojoNodesRemover;
import de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenGraphProvider;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoAttribute;

public class MergedPreferenceScreenProvider {

    private final SearchableInfoAttribute searchableInfoAttribute;
    private final boolean cacheMergedPreferenceScreens;
    private final FragmentFactoryAndInitializer fragmentFactoryAndInitializer;
    private final Context context;

    private static MergedPreferenceScreen mergedPreferenceScreen;

    public MergedPreferenceScreenProvider(final SearchableInfoAttribute searchableInfoAttribute,
                                          final boolean cacheMergedPreferenceScreens,
                                          final FragmentFactoryAndInitializer fragmentFactoryAndInitializer,
                                          final Context context) {
        this.searchableInfoAttribute = searchableInfoAttribute;
        this.cacheMergedPreferenceScreens = cacheMergedPreferenceScreens;
        this.fragmentFactoryAndInitializer = fragmentFactoryAndInitializer;
        this.context = context;
    }

    public MergedPreferenceScreen getMergedPreferenceScreen(final SearchablePreferenceScreenGraphProvider searchablePreferenceScreenGraphProvider) {
        if (!cacheMergedPreferenceScreens) {
            return computeMergedPreferenceScreen(searchablePreferenceScreenGraphProvider);
        }
        if (mergedPreferenceScreen == null) {
            mergedPreferenceScreen = computeMergedPreferenceScreen(searchablePreferenceScreenGraphProvider);
        }
        return mergedPreferenceScreen;
    }

    private MergedPreferenceScreen computeMergedPreferenceScreen(final SearchablePreferenceScreenGraphProvider searchablePreferenceScreenGraphProvider) {
        final Graph<PreferenceScreenWithHostClass, PreferenceEdge> entityGraph =
                searchablePreferenceScreenGraphProvider.getSearchablePreferenceScreenGraph();
        return getMergedPreferenceScreen(
                Graph2POJOGraphTransformer.transformGraph2POJOGraph(entityGraph),
                getPreferenceManager(entityGraph));
    }

    // FK-TODO: refactor
    private MergedPreferenceScreen getMergedPreferenceScreen(
            final Graph<PreferenceScreenWithHostClassPOJOWithMap, SearchablePreferencePOJOEdge> pojoGraph,
            final PreferenceManager preferenceManager) {
        final PreferenceScreenWithMap preferenceScreenWithMap =
                SearchablePreferenceScreenFromPOJOConverter.convertFromPOJO(
                        concat(withoutHostAndMap(new ArrayList<>(pojoGraph.vertexSet()))),
                        preferenceManager);
        return new MergedPreferenceScreen(
                preferenceScreenWithMap.preferenceScreen(),
                preferenceScreenWithMap.pojoEntityMap(),
                Set.of(),
                convertPojoKeys2EntityKeys(
                        PreferencePathByPreferenceProvider.getPreferencePathByPreference(
                                HostClassAndMapFromNodesRemover.removeHostClassAndMapFromNodes(pojoGraph)),
                        preferenceScreenWithMap.pojoEntityMap()),
                searchableInfoAttribute,
                new PreferencePathNavigator(
                        getHostByPreference(pojoGraph),
                        fragmentFactoryAndInitializer,
                        preferenceScreenWithMap.pojoEntityMap(),
                        context));
    }

    private static List<SearchablePreferenceScreenPOJO> withoutHostAndMap(final List<PreferenceScreenWithHostClassPOJOWithMap> preferenceScreens) {
        return preferenceScreens
                .stream()
                .map(preferenceScreenWithHostClassPOJOWithMap ->
                        preferenceScreenWithHostClassPOJOWithMap
                                .preferenceScreenWithHostClass()
                                .preferenceScreen())
                .collect(Collectors.toList());
    }

    private static PreferenceManager getPreferenceManager(final Graph<PreferenceScreenWithHostClass, PreferenceEdge> entityGraph) {
        return new ArrayList<>(entityGraph.vertexSet()).get(0).preferenceScreen().getPreferenceManager();
    }

    private Map<Preference, PreferencePath> convertPojoKeys2EntityKeys(
            final Map<SearchablePreferencePOJO, PreferencePath> preferencePathByPreference,
            final BiMap<SearchablePreferencePOJO, SearchablePreference> pojoEntityMap) {
        return preferencePathByPreference
                .entrySet()
                .stream()
                .collect(
                        Collectors.toMap(
                                entry -> pojoEntityMap.get(entry.getKey()),
                                Entry::getValue));
    }

    private static Map<SearchablePreferencePOJO, Class<? extends PreferenceFragmentCompat>> getHostByPreference(
            final Graph<PreferenceScreenWithHostClassPOJOWithMap, SearchablePreferencePOJOEdge> pojoGraph) {
        return HostByPreferenceProvider.getHostByPreference(
                MapFromPojoNodesRemover
                        .removeMapFromPojoNodes(pojoGraph)
                        .vertexSet());
    }

    private static SearchablePreferenceScreenPOJO concat(final List<SearchablePreferenceScreenPOJO> screens) {
        return new SearchablePreferenceScreenPOJO(
                "title of merged screen",
                "summary of merged screen",
                concatChildren(screens));
    }

    private static List<SearchablePreferencePOJO> concatChildren(final List<SearchablePreferenceScreenPOJO> screens) {
        return Lists.concat(
                screens
                        .stream()
                        .map(SearchablePreferenceScreenPOJO::children)
                        .collect(Collectors.toList()));
    }
}
