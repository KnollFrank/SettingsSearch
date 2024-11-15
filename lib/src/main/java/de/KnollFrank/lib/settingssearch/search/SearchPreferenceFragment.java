package de.KnollFrank.lib.settingssearch.search;

import static de.KnollFrank.lib.settingssearch.fragment.Fragments.showFragment;

import android.view.View;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;

import java.util.Locale;
import java.util.function.Consumer;

import de.KnollFrank.lib.settingssearch.MergedPreferenceScreen;
import de.KnollFrank.lib.settingssearch.R;
import de.KnollFrank.lib.settingssearch.client.SearchConfiguration;
import de.KnollFrank.lib.settingssearch.common.Keyboard;
import de.KnollFrank.lib.settingssearch.common.task.LongRunningTask;
import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunnerFactory;
import de.KnollFrank.lib.settingssearch.provider.IncludePreferenceInSearchResultsPredicate;
import de.KnollFrank.lib.settingssearch.provider.PrepareShow;
import de.KnollFrank.lib.settingssearch.provider.ShowPreferencePathPredicate;
import de.KnollFrank.lib.settingssearch.results.SearchResultsPreferenceFragment;

public class SearchPreferenceFragment extends Fragment {

    private final ShowPreferencePathPredicate showPreferencePathPredicate;
    private final SearchConfiguration searchConfiguration;
    private final IncludePreferenceInSearchResultsPredicate includePreferenceInSearchResultsPredicate;
    private final PrepareShow prepareShow;
    private final MergedPreferenceScreenFactory mergedPreferenceScreenFactory;
    private final Locale locale;

    public SearchPreferenceFragment(final SearchConfiguration searchConfiguration,
                                    final ShowPreferencePathPredicate showPreferencePathPredicate,
                                    final IncludePreferenceInSearchResultsPredicate includePreferenceInSearchResultsPredicate,
                                    final PrepareShow prepareShow,
                                    final MergedPreferenceScreenFactory mergedPreferenceScreenFactory,
                                    final Locale locale) {
        super(R.layout.searchpreference_fragment);
        this.searchConfiguration = searchConfiguration;
        this.showPreferencePathPredicate = showPreferencePathPredicate;
        this.includePreferenceInSearchResultsPredicate = includePreferenceInSearchResultsPredicate;
        this.prepareShow = prepareShow;
        this.mergedPreferenceScreenFactory = mergedPreferenceScreenFactory;
        this.locale = locale;
    }

    // FK-TODO: remove progressContainer
    public static View progressContainer;

    @Override
    public void onResume() {
        super.onResume();
        // FK-TODO: pass progressContainer as a parameter to methods
        progressContainer = requireView().findViewById(R.id.progressContainer);
        final LongRunningTask<MergedPreferenceScreen> longRunningTask =
                new LongRunningTask<>(
                        this::getMergedPreferenceScreen,
                        mergedPreferenceScreen ->
                                showSearchResultsPreferenceFragment(
                                        mergedPreferenceScreen,
                                        searchResultsPreferenceFragment -> configureSearchView(mergedPreferenceScreen)),
                        OnUiThreadRunnerFactory.fromActivity(requireActivity()),
                        progressContainer);
        longRunningTask.execute();
    }

    private MergedPreferenceScreen getMergedPreferenceScreen() {
        return mergedPreferenceScreenFactory.getMergedPreferenceScreen(
                getChildFragmentManager(),
                locale,
                requireContext());
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
                        new PreferenceSearcher(
                                mergedPreferenceScreen.preferences(),
                                includePreferenceInSearchResultsPredicate,
                                mergedPreferenceScreen.hostByPreference()),
                        mergedPreferenceScreen.searchResultsDisplayer()));
        selectSearchView(searchView);
        searchView.setQuery(searchView.getQuery(), true);
    }

    private void selectSearchView(final SearchView searchView) {
        searchView.requestFocus();
        Keyboard.showKeyboard(getActivity(), searchView);
    }
}
