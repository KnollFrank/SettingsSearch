package de.KnollFrank.lib.settingssearch;

import android.content.Context;

import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import org.jgrapht.Graph;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.PreferencePOJOs;
import de.KnollFrank.lib.settingssearch.db.SearchableInfoAndDialogInfoProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.MergedPreferenceScreenData;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJOEdge;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentInitializer;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactoryAndInitializer;
import de.KnollFrank.lib.settingssearch.fragment.Fragments;
import de.KnollFrank.lib.settingssearch.fragment.PreferencePathNavigator;
import de.KnollFrank.lib.settingssearch.fragment.factory.FragmentFactoryAndInitializerWithCache;
import de.KnollFrank.lib.settingssearch.graph.DefaultSearchablePreferenceScreenGraphProvider;
import de.KnollFrank.lib.settingssearch.graph.HostClassFromPojoNodesRemover;
import de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenGraphProvider;
import de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenGraphProviderWrapper;
import de.KnollFrank.lib.settingssearch.provider.HostByPreferenceProvider;
import de.KnollFrank.lib.settingssearch.provider.IsPreferenceSearchable;
import de.KnollFrank.lib.settingssearch.provider.PreferenceConnected2PreferenceFragmentProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferencePathByPreference;
import de.KnollFrank.lib.settingssearch.provider.PreferenceScreenGraphAvailableListener;
import de.KnollFrank.lib.settingssearch.provider.SearchableDialogInfoOfProvider;
import de.KnollFrank.lib.settingssearch.results.SearchResultsDisplayerFactory;
import de.KnollFrank.lib.settingssearch.search.IsPreferenceVisibleAndSearchable;
import de.KnollFrank.lib.settingssearch.search.PreferenceManagerProvider;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoProvider;

public class MergedPreferenceScreenFactory {

    private final Context context;
    private final SearchablePreferenceScreenGraphProviderWrapper searchablePreferenceScreenGraphProviderWrapper;
    private final FragmentFactoryAndInitializer fragmentFactoryAndInitializer;
    private final PreferenceManager preferenceManager;
    private final SearchablePreferenceScreenGraphProvider searchablePreferenceScreenGraphProvider;

    public MergedPreferenceScreenFactory(
            final FragmentManager fragmentManager,
            final Context context,
            final IsPreferenceSearchable isPreferenceSearchable,
            final SearchableInfoProvider searchableInfoProvider,
            final FragmentFactory fragmentFactory,
            final Class<? extends PreferenceFragmentCompat> rootPreferenceFragment,
            final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider,
            final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener,
            final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider,
            final SearchablePreferenceScreenGraphProviderWrapper searchablePreferenceScreenGraphProviderWrapper) {
        this.context = context;
        this.searchablePreferenceScreenGraphProviderWrapper = searchablePreferenceScreenGraphProviderWrapper;
        final DefaultFragmentInitializer defaultFragmentInitializer =
                new DefaultFragmentInitializer(
                        fragmentManager,
                        R.id.dummyFragmentContainerView);
        this.fragmentFactoryAndInitializer =
                new FragmentFactoryAndInitializer(
                        fragmentFactory,
                        defaultFragmentInitializer);
        final Fragments fragments =
                new Fragments(
                        new FragmentFactoryAndInitializerWithCache(fragmentFactoryAndInitializer),
                        context);
        this.preferenceManager =
                PreferenceManagerProvider.getPreferenceManager(
                        fragments,
                        rootPreferenceFragment);
        this.searchablePreferenceScreenGraphProvider =
                new DefaultSearchablePreferenceScreenGraphProvider(
                        rootPreferenceFragment.getName(),
                        new PreferenceScreenWithHostProvider(
                                fragments,
                                new SearchablePreferenceScreenProvider(
                                        new IsPreferenceVisibleAndSearchable(
                                                isPreferenceSearchable))),
                        preferenceConnected2PreferenceFragmentProvider,
                        preferenceScreenGraphAvailableListener,
                        new SearchableInfoAndDialogInfoProvider(
                                searchableInfoProvider,
                                new SearchableDialogInfoOfProvider(
                                        defaultFragmentInitializer,
                                        preferenceDialogAndSearchableInfoProvider)));
    }

    public MergedPreferenceScreen getMergedPreferenceScreen(final MergedPreferenceScreenData mergedPreferenceScreenData) {
        return getMergedPreferenceScreen(
                preferenceManager,
                fragmentFactoryAndInitializer,
                mergedPreferenceScreenData);
    }

    public static MergedPreferenceScreen getMergedPreferenceScreen(
            final PreferenceManager preferenceManager,
            final FragmentFactoryAndInitializer fragmentFactoryAndInitializer,
            final MergedPreferenceScreenData mergedPreferenceScreenData) {
        return new MergedPreferenceScreen(
                mergedPreferenceScreenData.allPreferencesForSearch(),
                SearchResultsDisplayerFactory.createSearchResultsDisplayer(
                        preferenceManager,
                        pojoEntityMap ->
                                PreferencePathByPreference.getPreferencePathByPreference(
                                        pojoEntityMap,
                                        mergedPreferenceScreenData.preferencePathByPreference())),
                new PreferencePathNavigator(
                        mergedPreferenceScreenData.hostByPreference(),
                        fragmentFactoryAndInitializer,
                        preferenceManager.getContext()));
    }

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

    public Graph<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> getSearchablePreferenceScreenGraph() {
        return searchablePreferenceScreenGraphProviderWrapper
                .wrap(
                        searchablePreferenceScreenGraphProvider,
                        context)
                .getSearchablePreferenceScreenGraph();
    }
}
