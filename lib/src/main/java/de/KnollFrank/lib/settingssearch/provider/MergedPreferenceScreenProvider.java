package de.KnollFrank.lib.settingssearch.provider;

import static de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClass2POJOConverter.PreferenceScreenWithHostClassPOJOWithMap;
import static de.KnollFrank.lib.settingssearch.provider.PreferenceScreensMerger.PreferenceScreenAndNonClickablePreferences;

import android.content.Context;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import com.codepoetics.protonpack.StreamUtils;
import com.google.common.collect.BiMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.ConnectedSearchablePreferenceScreens;
import de.KnollFrank.lib.settingssearch.MergedPreferenceScreen;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostClass;
import de.KnollFrank.lib.settingssearch.common.Lists;
import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.converter.IdGenerator;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClass2POJOConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenPOJO;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactoryAndInitializer;
import de.KnollFrank.lib.settingssearch.fragment.PreferencePathNavigator;
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
        return computeMergedPreferenceScreen(
                ConnectedSearchablePreferenceScreens.fromSearchablePreferenceScreenGraph(
                        searchablePreferenceScreenGraphProvider.getSearchablePreferenceScreenGraph()));
    }

    private MergedPreferenceScreen computeMergedPreferenceScreen(final ConnectedSearchablePreferenceScreens screens) {
        // MUST compute A (which just reads screens) before B (which modifies screens)
        // A:
        final Map<Preference, Class<? extends PreferenceFragmentCompat>> hostByPreference =
                HostByPreferenceProvider.getHostByPreference(screens.connectedSearchablePreferenceScreens());
        final BiMap<SearchablePreferencePOJO, SearchablePreference> pojoEntityMap =
                MergedPreferenceScreenProvider
                        .merge(screens.connectedSearchablePreferenceScreens())
                        .pojoEntityMap();
        // B:
        final PreferenceScreenAndNonClickablePreferences preferenceScreenAndNonClickablePreferences = destructivelyMergeScreens(screens.connectedSearchablePreferenceScreens());
        return new MergedPreferenceScreen(
                preferenceScreenAndNonClickablePreferences.preferenceScreen(),
                pojoEntityMap,
                preferenceScreenAndNonClickablePreferences.nonClickablePreferences(),
                screens.preferencePathByPreference(),
                searchableInfoAttribute,
                new PreferencePathNavigator(hostByPreference, fragmentFactoryAndInitializer, context));
    }

    private PreferenceScreenAndNonClickablePreferences destructivelyMergeScreens(final Set<PreferenceScreenWithHostClass> screens) {
        return preferenceScreensMerger.destructivelyMergeScreens(getPreferenceScreens(new ArrayList<>(screens)));
    }

    private record SearchablePreferenceScreenPOJOWithMap(
            SearchablePreferenceScreenPOJO searchablePreferenceScreen,
            BiMap<SearchablePreferencePOJO, SearchablePreference> pojoEntityMap) {
    }

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

    private static List<PreferenceScreen> getPreferenceScreens(final List<PreferenceScreenWithHostClass> screens) {
        return screens
                .stream()
                .map(PreferenceScreenWithHostClass::preferenceScreen)
                .collect(Collectors.toList());
    }
}
