package de.KnollFrank.lib.settingssearch.search;

import static de.KnollFrank.lib.settingssearch.fragment.Fragments.showFragment;

import android.view.View;
import android.widget.SearchView;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.Locale;
import java.util.function.Consumer;

import de.KnollFrank.lib.settingssearch.MergedPreferenceScreen;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.R;
import de.KnollFrank.lib.settingssearch.client.SearchConfiguration;
import de.KnollFrank.lib.settingssearch.common.Keyboard;
import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunner;
import de.KnollFrank.lib.settingssearch.common.task.Tasks;
import de.KnollFrank.lib.settingssearch.graph.PreferenceScreenGraphListener;
import de.KnollFrank.lib.settingssearch.provider.IncludePreferenceInSearchResultsPredicate;
import de.KnollFrank.lib.settingssearch.provider.PrepareShow;
import de.KnollFrank.lib.settingssearch.provider.ShowPreferencePathPredicate;
import de.KnollFrank.lib.settingssearch.results.recyclerview.SearchResultsFragment;
import de.KnollFrank.lib.settingssearch.search.progress.IProgressDisplayer;
import de.KnollFrank.lib.settingssearch.search.progress.ProgressDisplayerFactory;
import de.KnollFrank.lib.settingssearch.search.progress.ProgressProvider;

public class SearchPreferenceFragment extends Fragment {

    private final ShowPreferencePathPredicate showPreferencePathPredicate;
    private final SearchConfiguration searchConfiguration;
    private final IncludePreferenceInSearchResultsPredicate includePreferenceInSearchResultsPredicate;
    private final PrepareShow prepareShow;
    private final MergedPreferenceScreenFactory mergedPreferenceScreenFactory;
    private final Locale locale;
    private final OnUiThreadRunner onUiThreadRunner;

    public SearchPreferenceFragment(final SearchConfiguration searchConfiguration,
                                    final ShowPreferencePathPredicate showPreferencePathPredicate,
                                    final IncludePreferenceInSearchResultsPredicate includePreferenceInSearchResultsPredicate,
                                    final PrepareShow prepareShow,
                                    final MergedPreferenceScreenFactory mergedPreferenceScreenFactory,
                                    final Locale locale,
                                    final OnUiThreadRunner onUiThreadRunner) {
        super(R.layout.searchpreference_fragment);
        this.searchConfiguration = searchConfiguration;
        this.showPreferencePathPredicate = showPreferencePathPredicate;
        this.includePreferenceInSearchResultsPredicate = includePreferenceInSearchResultsPredicate;
        this.prepareShow = prepareShow;
        this.mergedPreferenceScreenFactory = mergedPreferenceScreenFactory;
        this.locale = locale;
        this.onUiThreadRunner = onUiThreadRunner;
    }

    @Override
    public void onResume() {
        super.onResume();
        final View progressContainer = requireView().findViewById(R.id.progressContainer);
        Tasks.execute(
                () -> getMergedPreferenceScreen(
                        searchConfiguration.fragmentContainerViewId(),
                        prepareShow,
                        requireActivity().getSupportFragmentManager(),
                        ProgressDisplayerFactory.createOnUiThreadProgressDisplayer(progressContainer, onUiThreadRunner)),
                mergedPreferenceScreen ->
                        showSearchResultsFragment(
                                mergedPreferenceScreen.searchResultsDisplayer().getSearchResultsFragment(),
                                searchResultsPreferenceFragment -> configureSearchView(mergedPreferenceScreen)),
                progressContainer);
    }

    private MergedPreferenceScreen getMergedPreferenceScreen(
            final @IdRes int fragmentContainerViewId,
            final PrepareShow prepareShow,
            final FragmentManager fragmentManager,
            final IProgressDisplayer progressDisplayer) {
        return mergedPreferenceScreenFactory.getMergedPreferenceScreen(
                fragmentContainerViewId,
                prepareShow,
                fragmentManager,
                getChildFragmentManager(),
                locale,
                onUiThreadRunner,
                progressDisplayer,
                new PreferenceScreenGraphListener() {

                    @Override
                    public void preferenceScreenWithHostAdded(final PreferenceScreenWithHost preferenceScreenWithHost) {
                        progressDisplayer.displayProgress(ProgressProvider.getProgress(preferenceScreenWithHost));
                    }
                });
    }

    private void showSearchResultsFragment(final SearchResultsFragment searchResultsFragment,
                                           final Consumer<SearchResultsFragment> onFragmentStarted) {
        showFragment(
//                SearchResultsPreferenceFragment.createInstance(
//                        mergedPreferenceScreen.searchResultsDisplayer(),
//                        mergedPreferenceScreen.preferencePathNavigator(),
//                        searchConfiguration.fragmentContainerViewId(),
//                        showPreferencePathPredicate,
//                        prepareShow),
                searchResultsFragment,
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
