package de.KnollFrank.lib.settingssearch.search;

import static de.KnollFrank.lib.settingssearch.fragment.Fragments.showFragment;

import android.widget.SearchView;

import androidx.fragment.app.Fragment;

import java.util.function.Consumer;

import de.KnollFrank.lib.settingssearch.MergedPreferenceScreen;
import de.KnollFrank.lib.settingssearch.R;
import de.KnollFrank.lib.settingssearch.client.SearchConfiguration;
import de.KnollFrank.lib.settingssearch.common.Keyboard;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;
import de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenGraphProviderWrapper;
import de.KnollFrank.lib.settingssearch.provider.IsPreferenceSearchable;
import de.KnollFrank.lib.settingssearch.provider.PreferenceConnected2PreferenceFragmentProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceScreenGraphAvailableListener;
import de.KnollFrank.lib.settingssearch.provider.PrepareShow;
import de.KnollFrank.lib.settingssearch.provider.ShowPreferencePathPredicate;
import de.KnollFrank.lib.settingssearch.results.SearchResultsPreferenceFragment;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoProvider;

public class SearchPreferenceFragment extends Fragment {

    private final IsPreferenceSearchable isPreferenceSearchable;
    private final SearchableInfoProvider searchableInfoProvider;
    private final ShowPreferencePathPredicate showPreferencePathPredicate;
    private final FragmentFactory fragmentFactory;
    private final SearchConfiguration searchConfiguration;
    private final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider;
    private final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener;
    private final PrepareShow prepareShow;
    private final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider;
    private final SearchablePreferenceScreenGraphProviderWrapper searchablePreferenceScreenGraphProviderWrapper;


    public SearchPreferenceFragment(final SearchConfiguration searchConfiguration,
                                    final IsPreferenceSearchable isPreferenceSearchable,
                                    final SearchableInfoProvider searchableInfoProvider,
                                    final ShowPreferencePathPredicate showPreferencePathPredicate,
                                    final FragmentFactory fragmentFactory,
                                    final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider,
                                    final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener,
                                    final PrepareShow prepareShow,
                                    final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider,
                                    final SearchablePreferenceScreenGraphProviderWrapper searchablePreferenceScreenGraphProviderWrapper) {
        super(R.layout.searchpreference_fragment);
        this.searchConfiguration = searchConfiguration;
        this.isPreferenceSearchable = isPreferenceSearchable;
        this.searchableInfoProvider = searchableInfoProvider;
        this.showPreferencePathPredicate = showPreferencePathPredicate;
        this.fragmentFactory = fragmentFactory;
        this.preferenceDialogAndSearchableInfoProvider = preferenceDialogAndSearchableInfoProvider;
        this.preferenceScreenGraphAvailableListener = preferenceScreenGraphAvailableListener;
        this.prepareShow = prepareShow;
        this.preferenceConnected2PreferenceFragmentProvider = preferenceConnected2PreferenceFragmentProvider;
        this.searchablePreferenceScreenGraphProviderWrapper = searchablePreferenceScreenGraphProviderWrapper;
    }

    @Override
    public void onResume() {
        super.onResume();
        final MergedPreferenceScreen mergedPreferenceScreen =
                new MergedPreferenceScreenFactory(
                        this,
                        searchConfiguration.rootPreferenceFragment(),
                        fragmentFactory,
                        searchablePreferenceScreenGraphProviderWrapper,
                        isPreferenceSearchable,
                        preferenceConnected2PreferenceFragmentProvider,
                        preferenceScreenGraphAvailableListener,
                        searchableInfoProvider,
                        preferenceDialogAndSearchableInfoProvider)
                        .getMergedPreferenceScreen();
        showSearchResultsPreferenceFragment(
                mergedPreferenceScreen,
                searchResultsPreferenceFragment -> configureSearchView(mergedPreferenceScreen));
    }

    private void showSearchResultsPreferenceFragment(final MergedPreferenceScreen mergedPreferenceScreen,
                                                     final Consumer<SearchResultsPreferenceFragment> onFragmentStarted) {
        showFragment(
                new SearchResultsPreferenceFragment(
                        mergedPreferenceScreen.searchResultsDisplayer(),
                        mergedPreferenceScreen.preferencePathNavigator(),
                        searchConfiguration.fragmentContainerViewId(),
                        showPreferencePathPredicate,
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
                        new PreferenceSearcher(mergedPreferenceScreen.allPreferencesForSearch()),
                        mergedPreferenceScreen.searchResultsDisplayer()));
        selectSearchView(searchView);
        searchView.setQuery(searchView.getQuery(), true);
    }

    private void selectSearchView(final SearchView searchView) {
        searchView.requestFocus();
        Keyboard.showKeyboard(getActivity(), searchView);
    }
}
