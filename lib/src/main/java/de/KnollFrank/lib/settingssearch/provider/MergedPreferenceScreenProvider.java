package de.KnollFrank.lib.settingssearch.provider;

import static de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClass2POJOConverter.PreferenceScreenWithHostClassPOJOWithMap;

import android.content.Context;

import androidx.preference.PreferenceManager;

import com.google.common.collect.BiMap;

import org.jgrapht.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.MergedPreferenceScreen;
import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostClass;
import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceScreenFromPOJOConverter.PreferenceScreenWithMap;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJOEdge;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactoryAndInitializer;
import de.KnollFrank.lib.settingssearch.fragment.PreferencePathNavigator;
import de.KnollFrank.lib.settingssearch.graph.Graph2POJOGraphTransformer;
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

    private static PreferenceManager getPreferenceManager(final Graph<PreferenceScreenWithHostClass, PreferenceEdge> entityGraph) {
        return new ArrayList<>(entityGraph.vertexSet()).get(0).preferenceScreen().getPreferenceManager();
    }

    private MergedPreferenceScreen getMergedPreferenceScreen(
            final Graph<PreferenceScreenWithHostClassPOJOWithMap, SearchablePreferencePOJOEdge> pojoGraph,
            final PreferenceManager preferenceManager) {
        final PreferenceScreenWithMap preferenceScreenWithMap =
                PreferenceScreenWithMapFactory.getPreferenceScreenWithMap(
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

    private static List<PreferenceScreenWithHostClassPOJO> withoutMap(final List<PreferenceScreenWithHostClassPOJOWithMap> screens) {
        return screens
                .stream()
                .map(MapFromPojoNodesRemover::removeMapFromPojoNode)
                .collect(Collectors.toList());
    }

    private PreferencePathNavigator getPreferencePathNavigator(
            final List<PreferenceScreenWithHostClassPOJOWithMap> screens,
            final BiMap<SearchablePreferencePOJO, SearchablePreference> pojoEntityMap) {
        return new PreferencePathNavigator(
                HostByPreferenceProvider.getHostByPreference(withoutMap(screens)),
                fragmentFactoryAndInitializer,
                pojoEntityMap,
                context);
    }
}
