package de.KnollFrank.lib.settingssearch.search;

import static de.KnollFrank.lib.settingssearch.fragment.Fragments.showFragment;

import android.content.Context;
import android.os.Bundle;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.annotation.RawRes;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import java.util.Optional;
import java.util.function.Consumer;

import de.KnollFrank.lib.settingssearch.MergedPreferenceScreen;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.R;
import de.KnollFrank.lib.settingssearch.SearchConfigurations;
import de.KnollFrank.lib.settingssearch.SearchablePreferenceScreenProvider;
import de.KnollFrank.lib.settingssearch.client.SearchConfiguration;
import de.KnollFrank.lib.settingssearch.common.Keyboard;
import de.KnollFrank.lib.settingssearch.db.SearchableInfoAndDialogInfoProvider;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentInitializer;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactoryAndInitializer;
import de.KnollFrank.lib.settingssearch.fragment.Fragments;
import de.KnollFrank.lib.settingssearch.fragment.factory.FragmentFactoryAndInitializerWithCache;
import de.KnollFrank.lib.settingssearch.graph.PreferenceScreensProvider;
import de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenGraphDAOProvider;
import de.KnollFrank.lib.settingssearch.provider.ISearchableDialogInfoOfProvider;
import de.KnollFrank.lib.settingssearch.provider.IsPreferenceSearchable;
import de.KnollFrank.lib.settingssearch.provider.MergedPreferenceScreenProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceConnected2PreferenceFragmentProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceScreenGraphAvailableListener;
import de.KnollFrank.lib.settingssearch.provider.PreferenceScreensMerger;
import de.KnollFrank.lib.settingssearch.provider.PrepareShow;
import de.KnollFrank.lib.settingssearch.provider.SearchableDialogInfoOfProvider;
import de.KnollFrank.lib.settingssearch.provider.ShowPreferencePath;
import de.KnollFrank.lib.settingssearch.results.SearchResultsPreferenceFragment;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoAttribute;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoProvider;

public class SearchPreferenceFragment extends Fragment {

    private final IsPreferenceSearchable isPreferenceSearchable;
    private final SearchableInfoProvider searchableInfoProvider;
    private final SearchableInfoAttribute searchableInfoAttribute;
    private final ShowPreferencePath showPreferencePath;
    private final FragmentFactory fragmentFactory;
    private SearchConfiguration searchConfiguration;
    private final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider;
    private final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener;
    private final PrepareShow prepareShow;
    private final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider;
    private final SearchablePreferenceScreenGraphDAOProvider.Mode mode;
    private final @RawRes int searchablePreferenceScreenGraph;

    public static SearchPreferenceFragment newInstance(
            final SearchConfiguration searchConfiguration,
            final IsPreferenceSearchable isPreferenceSearchable,
            final SearchableInfoProvider searchableInfoProvider,
            final SearchableInfoAttribute searchableInfoAttribute,
            final ShowPreferencePath showPreferencePath,
            final FragmentFactory fragmentFactory,
            final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider,
            final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener,
            final PrepareShow prepareShow,
            final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider,
            final SearchablePreferenceScreenGraphDAOProvider.Mode mode,
            final @RawRes int searchablePreferenceScreenGraph) {
        final SearchPreferenceFragment searchPreferenceFragment =
                new SearchPreferenceFragment(
                        isPreferenceSearchable,
                        searchableInfoProvider,
                        searchableInfoAttribute,
                        showPreferencePath,
                        fragmentFactory,
                        preferenceDialogAndSearchableInfoProvider,
                        preferenceScreenGraphAvailableListener,
                        prepareShow,
                        preferenceConnected2PreferenceFragmentProvider,
                        mode,
                        searchablePreferenceScreenGraph);
        searchPreferenceFragment.setArguments(SearchConfigurations.toBundle(searchConfiguration));
        return searchPreferenceFragment;
    }

    private SearchPreferenceFragment(final IsPreferenceSearchable isPreferenceSearchable,
                                     final SearchableInfoProvider searchableInfoProvider,
                                     final SearchableInfoAttribute searchableInfoAttribute,
                                     final ShowPreferencePath showPreferencePath,
                                     final FragmentFactory fragmentFactory,
                                     final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider,
                                     final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener,
                                     final PrepareShow prepareShow,
                                     final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider,
                                     final SearchablePreferenceScreenGraphDAOProvider.Mode mode,
                                     final @RawRes int searchablePreferenceScreenGraph) {
        super(R.layout.searchpreference_fragment);
        this.isPreferenceSearchable = isPreferenceSearchable;
        this.searchableInfoProvider = searchableInfoProvider;
        this.searchableInfoAttribute = searchableInfoAttribute;
        this.showPreferencePath = showPreferencePath;
        this.fragmentFactory = fragmentFactory;
        this.preferenceDialogAndSearchableInfoProvider = preferenceDialogAndSearchableInfoProvider;
        this.preferenceScreenGraphAvailableListener = preferenceScreenGraphAvailableListener;
        this.prepareShow = prepareShow;
        this.preferenceConnected2PreferenceFragmentProvider = preferenceConnected2PreferenceFragmentProvider;
        this.mode = mode;
        this.searchablePreferenceScreenGraph = searchablePreferenceScreenGraph;
    }

    public SearchPreferenceFragment() {
        this(
                (preference, host) -> true,
                preference -> Optional.empty(),
                new SearchableInfoAttribute(),
                preferencePath -> true,
                new DefaultFragmentFactory(),
                (preference, hostOfPreference) -> Optional.empty(),
                preferenceScreenGraph -> {
                },
                preferenceFragmentCompat -> {
                },
                (preference, hostOfPreference) -> Optional.empty(),
                SearchablePreferenceScreenGraphDAOProvider.Mode.COMPUTE_AND_PERSIST_GRAPH,
                ResourcesCompat.ID_NULL);
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchConfiguration = SearchConfigurations.fromBundle(requireArguments());
    }

    @Override
    public void onResume() {
        super.onResume();
        final MergedPreferenceScreen mergedPreferenceScreen = getMergedPreferenceScreen();
        showSearchResultsPreferenceFragment(
                mergedPreferenceScreen,
                searchResultsPreferenceFragment -> configureSearchView(mergedPreferenceScreen));
    }

    private MergedPreferenceScreen getMergedPreferenceScreen() {
        final DefaultFragmentInitializer defaultFragmentInitializer = new DefaultFragmentInitializer(getChildFragmentManager(), R.id.dummyFragmentContainerView);
        final FragmentFactoryAndInitializer fragmentFactoryAndInitializer =
                new FragmentFactoryAndInitializer(
                        fragmentFactory,
                        defaultFragmentInitializer);
        final Context context = requireActivity();
        final Fragments fragments =
                new Fragments(
                        new FragmentFactoryAndInitializerWithCache(fragmentFactoryAndInitializer),
                        context);
        final ISearchableDialogInfoOfProvider searchableDialogInfoOfProvider =
                new SearchableDialogInfoOfProvider(
                        defaultFragmentInitializer,
                        preferenceDialogAndSearchableInfoProvider);
        final MergedPreferenceScreenProvider mergedPreferenceScreenProvider =
                new MergedPreferenceScreenProvider(
                        new PreferenceScreensProvider(
                                new PreferenceScreenWithHostProvider(
                                        fragments,
                                        new SearchablePreferenceScreenProvider(
                                                new IsPreferenceVisibleAndSearchable(
                                                        isPreferenceSearchable))),
                                preferenceConnected2PreferenceFragmentProvider,
                                preferenceScreenGraphAvailableListener,
                                new SearchableInfoAndDialogInfoProvider(searchableInfoProvider, searchableDialogInfoOfProvider),
                                PreferenceManagerProvider.getPreferenceManager(fragments, searchConfiguration.rootPreferenceFragment())),
                        new PreferenceScreensMerger(getContext()),
                        searchableInfoAttribute,
                        true,
                        fragmentFactoryAndInitializer,
                        context);
        return mergedPreferenceScreenProvider.getMergedPreferenceScreen(searchConfiguration.rootPreferenceFragment().getName(), mode, searchablePreferenceScreenGraph);
    }

    private void showSearchResultsPreferenceFragment(final MergedPreferenceScreen mergedPreferenceScreen,
                                                     final Consumer<SearchResultsPreferenceFragment> onFragmentStarted) {
        showFragment(
                SearchResultsPreferenceFragment.newInstance(
                        searchConfiguration.fragmentContainerViewId(),
                        searchableInfoAttribute,
                        showPreferencePath,
                        mergedPreferenceScreen,
                        prepareShow),
                onFragmentStarted,
                false,
                R.id.searchResultsFragmentContainerView,
                getChildFragmentManager());
    }

    private void configureSearchView(final MergedPreferenceScreen mergedPreferenceScreen) {
        final SearchView searchView = requireView().findViewById(R.id.searchView);
        SearchViewConfigurer.configureSearchView(
                searchView,
                searchConfiguration.queryHint(),
                new SearchAndDisplay(
                        new PreferenceSearcher(
                                mergedPreferenceScreen,
                                searchableInfoAttribute),
                        searchableInfoAttribute,
                        mergedPreferenceScreen.searchablePreferenceScreen,
                        requireContext()));
        selectSearchView(searchView);
        searchView.setQuery(searchView.getQuery(), true);
    }

    private void selectSearchView(final SearchView searchView) {
        searchView.requestFocus();
        Keyboard.showKeyboard(getActivity(), searchView);
    }
}
