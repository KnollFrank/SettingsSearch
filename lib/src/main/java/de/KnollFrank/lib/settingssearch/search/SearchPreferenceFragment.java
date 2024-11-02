package de.KnollFrank.lib.settingssearch.search;

import static de.KnollFrank.lib.settingssearch.fragment.Fragments.showFragment;

import android.widget.SearchView;

import androidx.fragment.app.Fragment;

import java.util.function.Consumer;

import de.KnollFrank.lib.settingssearch.MergedPreferenceScreen;
import de.KnollFrank.lib.settingssearch.R;
import de.KnollFrank.lib.settingssearch.client.SearchConfiguration;
import de.KnollFrank.lib.settingssearch.common.Keyboard;
import de.KnollFrank.lib.settingssearch.provider.PrepareShow;
import de.KnollFrank.lib.settingssearch.provider.ShowPreferencePathPredicate;
import de.KnollFrank.lib.settingssearch.results.SearchResultsPreferenceFragment;

public class SearchPreferenceFragment extends Fragment {

    private final ShowPreferencePathPredicate showPreferencePathPredicate;
    private final SearchConfiguration searchConfiguration;
    private final PrepareShow prepareShow;
    private final MergedPreferenceScreenFactory mergedPreferenceScreenFactory;

    public SearchPreferenceFragment(final SearchConfiguration searchConfiguration,
                                    final ShowPreferencePathPredicate showPreferencePathPredicate,
                                    final PrepareShow prepareShow,
                                    final MergedPreferenceScreenFactory mergedPreferenceScreenFactory) {
        super(R.layout.searchpreference_fragment);
        this.searchConfiguration = searchConfiguration;
        this.showPreferencePathPredicate = showPreferencePathPredicate;
        this.prepareShow = prepareShow;
        this.mergedPreferenceScreenFactory = mergedPreferenceScreenFactory;
    }

    @Override
    public void onResume() {
        super.onResume();
        final MergedPreferenceScreen mergedPreferenceScreen =
                mergedPreferenceScreenFactory.getMergedPreferenceScreen(
                        getChildFragmentManager(),
                        requireContext());
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
                        new PreferenceSearcher(mergedPreferenceScreen.preferences()),
                        mergedPreferenceScreen.searchResultsDisplayer()));
        selectSearchView(searchView);
        searchView.setQuery(searchView.getQuery(), true);
    }

    private void selectSearchView(final SearchView searchView) {
        searchView.requestFocus();
        Keyboard.showKeyboard(getActivity(), searchView);
    }
}
