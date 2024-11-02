package de.KnollFrank.lib.settingssearch;

import android.content.Context;

import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import org.jgrapht.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.PreferencePOJOs;
import de.KnollFrank.lib.settingssearch.db.SearchableInfoAndDialogInfoProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJOEdge;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentInitializer;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactoryAndInitializer;
import de.KnollFrank.lib.settingssearch.fragment.Fragments;
import de.KnollFrank.lib.settingssearch.fragment.PreferenceDialogs;
import de.KnollFrank.lib.settingssearch.fragment.PreferencePathNavigator;
import de.KnollFrank.lib.settingssearch.fragment.factory.FragmentFactoryAndInitializerWithCache;
import de.KnollFrank.lib.settingssearch.graph.DefaultSearchablePreferenceScreenGraphProvider;
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

    private final FragmentManager fragmentManager;
    private final Context context;
    private final IsPreferenceSearchable isPreferenceSearchable;
    private final SearchableInfoProvider searchableInfoProvider;
    private final FragmentFactory fragmentFactory;
    private final Class<? extends PreferenceFragmentCompat> rootPreferenceFragment;
    private final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider;
    private final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener;
    private final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider;
    private final SearchablePreferenceScreenGraphProviderWrapper searchablePreferenceScreenGraphProviderWrapper;

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
        this.fragmentManager = fragmentManager;
        this.context = context;
        this.isPreferenceSearchable = isPreferenceSearchable;
        this.searchableInfoProvider = searchableInfoProvider;
        this.fragmentFactory = fragmentFactory;
        this.rootPreferenceFragment = rootPreferenceFragment;
        this.preferenceDialogAndSearchableInfoProvider = preferenceDialogAndSearchableInfoProvider;
        this.preferenceScreenGraphAvailableListener = preferenceScreenGraphAvailableListener;
        this.preferenceConnected2PreferenceFragmentProvider = preferenceConnected2PreferenceFragmentProvider;
        this.searchablePreferenceScreenGraphProviderWrapper = searchablePreferenceScreenGraphProviderWrapper;
    }

    public MergedPreferenceScreen getMergedPreferenceScreen() {
        final var defaultFragmentInitializer = getDefaultFragmentInitializer();
        final var fragmentFactoryAndInitializer = getFragmentFactoryAndInitializer(defaultFragmentInitializer);
        final var fragments = getFragments(fragmentFactoryAndInitializer);
        final PreferenceManager preferenceManager =
                PreferenceManagerProvider.getPreferenceManager(
                        fragments,
                        rootPreferenceFragment);
        return getMergedPreferenceScreen(
                getSearchablePreferenceScreenGraph(
                        defaultFragmentInitializer,
                        fragments,
                        context),
                preferenceManager,
                fragmentFactoryAndInitializer);
    }

    public static MergedPreferenceScreen getMergedPreferenceScreen(
            final Graph<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> pojoGraph,
            final PreferenceManager preferenceManager,
            final FragmentFactoryAndInitializer fragmentFactoryAndInitializer) {
        return new MergedPreferenceScreen(
                PreferencePOJOs.getPreferencesRecursively(getPreferences(pojoGraph.vertexSet())),
                SearchResultsDisplayerFactory.createSearchResultsDisplayer(
                        preferenceManager,
                        pojoEntityMap ->
                                PreferencePathByPreference.getPreferencePathByPreference(
                                        pojoGraph,
                                        pojoEntityMap)),
                getPreferencePathNavigator(
                        new ArrayList<>(pojoGraph.vertexSet()),
                        preferenceManager,
                        fragmentFactoryAndInitializer));
    }

    private static Set<SearchablePreferencePOJO> getPreferences(final Set<PreferenceScreenWithHostClassPOJO> preferenceScreens) {
        return preferenceScreens
                .stream()
                .flatMap(preferenceScreenWithHostClassPOJO -> preferenceScreenWithHostClassPOJO.preferenceScreen().children().stream())
                .collect(Collectors.toSet());
    }

    private static PreferencePathNavigator getPreferencePathNavigator(
            final List<PreferenceScreenWithHostClassPOJO> screens,
            final PreferenceManager preferenceManager,
            final FragmentFactoryAndInitializer fragmentFactoryAndInitializer) {
        return new PreferencePathNavigator(
                HostByPreferenceProvider.getHostByPreference(screens),
                fragmentFactoryAndInitializer,
                preferenceManager.getContext());
    }

    private DefaultFragmentInitializer getDefaultFragmentInitializer() {
        return new DefaultFragmentInitializer(
                fragmentManager,
                R.id.dummyFragmentContainerView);
    }

    private FragmentFactoryAndInitializer getFragmentFactoryAndInitializer(final DefaultFragmentInitializer defaultFragmentInitializer) {
        return new FragmentFactoryAndInitializer(
                fragmentFactory,
                defaultFragmentInitializer);
    }

    private Fragments getFragments(final FragmentFactoryAndInitializer fragmentFactoryAndInitializer) {
        return new Fragments(
                new FragmentFactoryAndInitializerWithCache(fragmentFactoryAndInitializer),
                context);
    }

    private Graph<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> getSearchablePreferenceScreenGraph(
            final PreferenceDialogs preferenceDialogs,
            final Fragments fragments,
            final Context context) {
        return searchablePreferenceScreenGraphProviderWrapper
                .wrap(
                        getSearchablePreferenceScreenGraphProvider(
                                preferenceDialogs,
                                fragments),
                        context)
                .getSearchablePreferenceScreenGraph();
    }

    private SearchablePreferenceScreenGraphProvider getSearchablePreferenceScreenGraphProvider(
            final PreferenceDialogs preferenceDialogs,
            final Fragments fragments) {
        return new DefaultSearchablePreferenceScreenGraphProvider(
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
                                preferenceDialogs,
                                preferenceDialogAndSearchableInfoProvider)));
    }
}
