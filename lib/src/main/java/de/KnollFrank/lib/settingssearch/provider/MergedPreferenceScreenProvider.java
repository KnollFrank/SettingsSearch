package de.KnollFrank.lib.settingssearch.provider;

import static de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClass2POJOConverter.PreferenceScreenWithHostClassPOJOWithMap;
import static de.KnollFrank.lib.settingssearch.provider.PreferenceScreensMerger.PreferenceScreenAndNonClickablePreferences;

import android.content.Context;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import com.codepoetics.protonpack.StreamUtils;
import com.google.common.collect.BiMap;

import org.jgrapht.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.MergedPreferenceScreen;
import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.PreferencePathByPreferenceProvider;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostClass;
import de.KnollFrank.lib.settingssearch.common.Lists;
import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.converter.IdGenerator;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClass2POJOConverter;
import de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceScreenPOJOWithMap;
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

    private final PreferenceScreensMerger preferenceScreensMerger;
    private final SearchableInfoAttribute searchableInfoAttribute;
    private final boolean cacheMergedPreferenceScreens;
    private final FragmentFactoryAndInitializer fragmentFactoryAndInitializer;
    private final Context context;

    private static MergedPreferenceScreen mergedPreferenceScreen;

    public MergedPreferenceScreenProvider(final PreferenceScreensMerger preferenceScreensMerger,
                                          final SearchableInfoAttribute searchableInfoAttribute,
                                          final boolean cacheMergedPreferenceScreens,
                                          final FragmentFactoryAndInitializer fragmentFactoryAndInitializer,
                                          final Context context) {
        this.preferenceScreensMerger = preferenceScreensMerger;
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
        final Graph<PreferenceScreenWithHostClassPOJOWithMap, SearchablePreferencePOJOEdge> pojoGraph =
                Graph2POJOGraphTransformer.transformGraph2POJOGraph(entityGraph);
        final BiMap<SearchablePreferencePOJO, SearchablePreference> pojoEntityMap =
                getPojoEntityMap(pojoGraph);
        final Map<Preference, PreferencePath> preferencePathByPreference =
                PreferencePathByPreferenceProvider.getPreferencePathByPreference(
                        HostClassAndMapFromNodesRemover.removeHostClassAndMapFromNodes(pojoGraph),
                        pojoEntityMap);
        final PreferenceScreenAndNonClickablePreferences preferenceScreenAndNonClickablePreferences =
                destructivelyMergeScreens(entityGraph.vertexSet());
        return new MergedPreferenceScreen(
                preferenceScreenAndNonClickablePreferences.preferenceScreen(),
                pojoEntityMap,
                preferenceScreenAndNonClickablePreferences.nonClickablePreferences(),
                preferencePathByPreference,
                searchableInfoAttribute,
                new PreferencePathNavigator(
                        getHostByPreference(pojoGraph),
                        fragmentFactoryAndInitializer,
                        pojoEntityMap,
                        context));
    }

    private static Map<SearchablePreferencePOJO, Class<? extends PreferenceFragmentCompat>> getHostByPreference(
            final Graph<PreferenceScreenWithHostClassPOJOWithMap, SearchablePreferencePOJOEdge> pojoGraph) {
        return HostByPreferenceProvider.getHostByPreference(
                MapFromPojoNodesRemover
                        .removeMapFromPojoNodes(pojoGraph)
                        .vertexSet());
    }

    private PreferenceScreenAndNonClickablePreferences destructivelyMergeScreens(final Set<PreferenceScreenWithHostClass> screens) {
        return preferenceScreensMerger.destructivelyMergeScreens(getPreferenceScreens(new ArrayList<>(screens)));
    }

    // FK-TODO: remove?
    private static SearchablePreferenceScreenPOJOWithMap merge(final Set<PreferenceScreenWithHostClass> screens) {
        final List<PreferenceScreenWithHostClassPOJOWithMap> entityWithMapList = getEntityWithMapList(screens);
        return new SearchablePreferenceScreenPOJOWithMap(
                new SearchablePreferenceScreenPOJO(
                        "title of merged screen",
                        "summary of merged screen",
                        getChildren(entityWithMapList)),
                getPojoEntityMap(entityWithMapList));
    }

    private static List<PreferenceScreenWithHostClassPOJOWithMap> getEntityWithMapList(final Set<PreferenceScreenWithHostClass> screens) {
        final IdGenerator idGenerator = new IdGenerator();
        return StreamUtils
                .zipWithIndex(screens.stream())
                .map(
                        preferenceScreenWithHostClassIndexed ->
                                PreferenceScreenWithHostClass2POJOConverter.convert2POJO(
                                        preferenceScreenWithHostClassIndexed.getValue(),
                                        Math.toIntExact(preferenceScreenWithHostClassIndexed.getIndex()),
                                        idGenerator))
                .collect(Collectors.toList());
    }

    private static List<SearchablePreferencePOJO> getChildren(final List<PreferenceScreenWithHostClassPOJOWithMap> entityWithMapList) {
        return Lists.concat(
                entityWithMapList
                        .stream()
                        .map(
                                preferenceScreenWithHostClassPOJOWithMap ->
                                        preferenceScreenWithHostClassPOJOWithMap
                                                .preferenceScreenWithHostClass()
                                                .preferenceScreen()
                                                .children())
                        .collect(Collectors.toList()));
    }

    private static BiMap<SearchablePreferencePOJO, SearchablePreference> getPojoEntityMap(final List<PreferenceScreenWithHostClassPOJOWithMap> entityWithMapList) {
        return Maps.mergeBiMaps(
                entityWithMapList
                        .stream()
                        .map(PreferenceScreenWithHostClassPOJOWithMap::pojoEntityMap)
                        .collect(Collectors.toList()));
    }

    private static BiMap<SearchablePreferencePOJO, SearchablePreference> getPojoEntityMap(
            final Graph<PreferenceScreenWithHostClassPOJOWithMap, SearchablePreferencePOJOEdge> pojoGraph) {
        return Maps.mergeBiMaps(
                pojoGraph
                        .vertexSet()
                        .stream()
                        .map(PreferenceScreenWithHostClassPOJOWithMap::pojoEntityMap)
                        .collect(Collectors.toList()));
    }

    private static List<PreferenceScreen> getPreferenceScreens(final List<PreferenceScreenWithHostClass> screens) {
        return screens
                .stream()
                .map(PreferenceScreenWithHostClass::preferenceScreen)
                .collect(Collectors.toList());
    }
}
