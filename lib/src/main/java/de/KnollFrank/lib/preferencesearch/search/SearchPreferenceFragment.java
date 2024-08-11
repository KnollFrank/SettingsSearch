package de.KnollFrank.lib.preferencesearch.search;

import static de.KnollFrank.lib.preferencesearch.fragment.Fragments.showFragment;

import android.os.Bundle;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;

import com.google.common.collect.ImmutableList;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import de.KnollFrank.lib.preferencesearch.MergedPreferenceScreen;
import de.KnollFrank.lib.preferencesearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.preferencesearch.PreferenceScreensProvider;
import de.KnollFrank.lib.preferencesearch.R;
import de.KnollFrank.lib.preferencesearch.SearchConfigurations;
import de.KnollFrank.lib.preferencesearch.client.SearchConfiguration;
import de.KnollFrank.lib.preferencesearch.common.Keyboard;
import de.KnollFrank.lib.preferencesearch.common.Maps;
import de.KnollFrank.lib.preferencesearch.fragment.DefaultFragmentFactory;
import de.KnollFrank.lib.preferencesearch.fragment.DefaultFragmentInitializer;
import de.KnollFrank.lib.preferencesearch.fragment.FragmentFactory;
import de.KnollFrank.lib.preferencesearch.fragment.FragmentFactoryAndInitializer;
import de.KnollFrank.lib.preferencesearch.fragment.Fragments;
import de.KnollFrank.lib.preferencesearch.fragment.factory.FragmentFactoryAndInitializerWithCache;
import de.KnollFrank.lib.preferencesearch.provider.IsPreferenceSearchable;
import de.KnollFrank.lib.preferencesearch.provider.MergedPreferenceScreenProvider;
import de.KnollFrank.lib.preferencesearch.provider.PreferenceDialogAndSearchableInfoProvider;
import de.KnollFrank.lib.preferencesearch.provider.PreferenceScreensMerger;
import de.KnollFrank.lib.preferencesearch.provider.ShowPreferencePath;
import de.KnollFrank.lib.preferencesearch.results.SearchResultsPreferenceFragment;
import de.KnollFrank.lib.preferencesearch.search.provider.PreferenceDescriptions;
import de.KnollFrank.lib.preferencesearch.search.provider.SearchableInfoAttribute;
import de.KnollFrank.lib.preferencesearch.search.provider.SearchableInfoProvider;
import de.KnollFrank.lib.preferencesearch.search.provider.SearchableInfoProviderInternal;

public class SearchPreferenceFragment extends Fragment {

    private final IsPreferenceSearchable isPreferenceSearchable;
    private final Map<Class<? extends Preference>, SearchableInfoProvider> searchableInfoProviderByPreferenceClass;
    private final SearchableInfoAttribute searchableInfoAttribute;
    private final ShowPreferencePath showPreferencePath;
    private final FragmentFactory fragmentFactory;
    private SearchConfiguration searchConfiguration;
    private final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider;

    public static SearchPreferenceFragment newInstance(
            final SearchConfiguration searchConfiguration,
            final IsPreferenceSearchable isPreferenceSearchable,
            final Map<Class<? extends Preference>, SearchableInfoProvider> searchableInfoProviderByPreferenceClass,
            final SearchableInfoAttribute searchableInfoAttribute,
            final ShowPreferencePath showPreferencePath,
            final FragmentFactory fragmentFactory,
            final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider) {
        final SearchPreferenceFragment searchPreferenceFragment =
                new SearchPreferenceFragment(
                        isPreferenceSearchable,
                        searchableInfoProviderByPreferenceClass,
                        searchableInfoAttribute,
                        showPreferencePath,
                        fragmentFactory,
                        preferenceDialogAndSearchableInfoProvider);
        searchPreferenceFragment.setArguments(SearchConfigurations.toBundle(searchConfiguration));
        return searchPreferenceFragment;
    }

    public SearchPreferenceFragment(final IsPreferenceSearchable isPreferenceSearchable,
                                    final Map<Class<? extends Preference>, SearchableInfoProvider> searchableInfoProviderByPreferenceClass,
                                    final SearchableInfoAttribute searchableInfoAttribute,
                                    final ShowPreferencePath showPreferencePath,
                                    final FragmentFactory fragmentFactory,
                                    final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider) {
        super(R.layout.searchpreference_fragment);
        this.isPreferenceSearchable = isPreferenceSearchable;
        this.searchableInfoProviderByPreferenceClass = searchableInfoProviderByPreferenceClass;
        this.searchableInfoAttribute = searchableInfoAttribute;
        this.showPreferencePath = showPreferencePath;
        this.fragmentFactory = fragmentFactory;
        this.preferenceDialogAndSearchableInfoProvider = preferenceDialogAndSearchableInfoProvider;
    }

    public SearchPreferenceFragment() {
        this(
                (preference, host) -> true,
                Collections.emptyMap(),
                new SearchableInfoAttribute(),
                preference -> true,
                new DefaultFragmentFactory(),
                (hostOfPreference, preference) -> Optional.empty());
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
        final Fragments fragments =
                new Fragments(
                        new FragmentFactoryAndInitializerWithCache(
                                new FragmentFactoryAndInitializer(
                                        fragmentFactory,
                                        defaultFragmentInitializer)),
                        requireActivity());
        final MergedPreferenceScreenProvider mergedPreferenceScreenProvider =
                new MergedPreferenceScreenProvider(
                        fragments,
                        defaultFragmentInitializer,
                        new PreferenceScreensProvider(new PreferenceScreenWithHostProvider(fragments)),
                        new PreferenceScreensMerger(getContext()),
                        isPreferenceSearchable,
                        searchableInfoAttribute,
                        preferenceDialogAndSearchableInfoProvider,
                        true);
        return mergedPreferenceScreenProvider.getMergedPreferenceScreen(searchConfiguration.rootPreferenceFragment.getName());
    }

    private void showSearchResultsPreferenceFragment(final MergedPreferenceScreen mergedPreferenceScreen,
                                                     final Consumer<SearchResultsPreferenceFragment> onFragmentStarted) {
        showFragment(
                SearchResultsPreferenceFragment.newInstance(
                        searchConfiguration.fragmentContainerViewId,
                        searchableInfoAttribute,
                        showPreferencePath,
                        mergedPreferenceScreen),
                onFragmentStarted,
                false,
                R.id.searchResultsFragmentContainerView,
                getChildFragmentManager());
    }

    private void configureSearchView(final MergedPreferenceScreen mergedPreferenceScreen) {
        final SearchView searchView = requireView().findViewById(R.id.searchView);
        SearchViewConfigurer.configureSearchView(
                searchView,
                searchConfiguration.textHint,
                new SearchAndDisplay(
                        new PreferenceSearcher(
                                mergedPreferenceScreen,
                                searchableInfoAttribute,
                                getSearchableInfoProviderInternal(mergedPreferenceScreen)),
                        searchableInfoAttribute,
                        mergedPreferenceScreen.preferenceScreen,
                        requireContext()));
        selectSearchView(searchView);
        searchView.setQuery(searchView.getQuery(), true);
    }

    private SearchableInfoProviderInternal getSearchableInfoProviderInternal(final MergedPreferenceScreen mergedPreferenceScreen) {
        return new SearchableInfoProviderInternal(
                Maps.merge(
                        ImmutableList.of(
                                searchableInfoProviderByPreferenceClass,
                                PreferenceDescriptions.getSearchableInfoProviderByPreferenceClass(mergedPreferenceScreen.getPreferenceDescriptions())),
                        SearchableInfoProvider::mergeWith));
    }

    private void selectSearchView(final SearchView searchView) {
        searchView.requestFocus();
        Keyboard.showKeyboard(getActivity(), searchView);
    }
}
