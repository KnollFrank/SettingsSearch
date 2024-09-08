package de.KnollFrank.lib.settingssearch.search;

import static de.KnollFrank.lib.settingssearch.fragment.Fragments.showFragment;

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

import de.KnollFrank.lib.settingssearch.MergedPreferenceScreen;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.PreferenceScreensProvider;
import de.KnollFrank.lib.settingssearch.R;
import de.KnollFrank.lib.settingssearch.SearchConfigurations;
import de.KnollFrank.lib.settingssearch.client.SearchConfiguration;
import de.KnollFrank.lib.settingssearch.common.Keyboard;
import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentInitializer;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactoryAndInitializer;
import de.KnollFrank.lib.settingssearch.fragment.Fragments;
import de.KnollFrank.lib.settingssearch.fragment.factory.FragmentFactoryAndInitializerWithCache;
import de.KnollFrank.lib.settingssearch.provider.IsPreferenceSearchable;
import de.KnollFrank.lib.settingssearch.provider.MergedPreferenceScreenProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceScreenGraphAvailableListener;
import de.KnollFrank.lib.settingssearch.provider.PreferenceScreensMerger;
import de.KnollFrank.lib.settingssearch.provider.PrepareShow;
import de.KnollFrank.lib.settingssearch.provider.ShowPreferencePath;
import de.KnollFrank.lib.settingssearch.results.SearchResultsPreferenceFragment;
import de.KnollFrank.lib.settingssearch.search.provider.PreferenceDescriptions;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoAttribute;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoProvider;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoProviderInternal;

public class SearchPreferenceFragment extends Fragment {

    private final IsPreferenceSearchable isPreferenceSearchable;
    private final Map<Class<? extends Preference>, SearchableInfoProvider> searchableInfoProviderByPreferenceClass;
    private final SearchableInfoAttribute searchableInfoAttribute;
    private final ShowPreferencePath showPreferencePath;
    private final FragmentFactory fragmentFactory;
    private SearchConfiguration searchConfiguration;
    private final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider;
    private final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener;
    private final PrepareShow prepareShow;

    public static SearchPreferenceFragment newInstance(
            final SearchConfiguration searchConfiguration,
            final IsPreferenceSearchable isPreferenceSearchable,
            final Map<Class<? extends Preference>, SearchableInfoProvider> searchableInfoProviderByPreferenceClass,
            final SearchableInfoAttribute searchableInfoAttribute,
            final ShowPreferencePath showPreferencePath,
            final FragmentFactory fragmentFactory,
            final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider,
            final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener,
            final PrepareShow prepareShow) {
        final SearchPreferenceFragment searchPreferenceFragment =
                new SearchPreferenceFragment(
                        isPreferenceSearchable,
                        searchableInfoProviderByPreferenceClass,
                        searchableInfoAttribute,
                        showPreferencePath,
                        fragmentFactory,
                        preferenceDialogAndSearchableInfoProvider,
                        preferenceScreenGraphAvailableListener,
                        prepareShow);
        searchPreferenceFragment.setArguments(SearchConfigurations.toBundle(searchConfiguration));
        return searchPreferenceFragment;
    }

    public SearchPreferenceFragment(final IsPreferenceSearchable isPreferenceSearchable,
                                    final Map<Class<? extends Preference>, SearchableInfoProvider> searchableInfoProviderByPreferenceClass,
                                    final SearchableInfoAttribute searchableInfoAttribute,
                                    final ShowPreferencePath showPreferencePath,
                                    final FragmentFactory fragmentFactory,
                                    final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider,
                                    final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener,
                                    final PrepareShow prepareShow) {
        super(R.layout.searchpreference_fragment);
        this.isPreferenceSearchable = isPreferenceSearchable;
        this.searchableInfoProviderByPreferenceClass = searchableInfoProviderByPreferenceClass;
        this.searchableInfoAttribute = searchableInfoAttribute;
        this.showPreferencePath = showPreferencePath;
        this.fragmentFactory = fragmentFactory;
        this.preferenceDialogAndSearchableInfoProvider = preferenceDialogAndSearchableInfoProvider;
        this.preferenceScreenGraphAvailableListener = preferenceScreenGraphAvailableListener;
        this.prepareShow = prepareShow;
    }

    public SearchPreferenceFragment() {
        this(
                (preference, host) -> true,
                Collections.emptyMap(),
                new SearchableInfoAttribute(),
                preferencePath -> true,
                new DefaultFragmentFactory(),
                (hostOfPreference, preference) -> Optional.empty(),
                preferenceScreenGraph -> {
                },
                preferenceFragmentCompat -> {
                });
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
                        preferenceScreenGraphAvailableListener,
                        true);
        return mergedPreferenceScreenProvider.getMergedPreferenceScreen(searchConfiguration.rootPreferenceFragment().getName());
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
                searchConfiguration.textHint(),
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
