package de.KnollFrank.lib.settingssearch.search;

import static de.KnollFrank.lib.settingssearch.fragment.Fragments.showFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import de.KnollFrank.lib.settingssearch.MergedPreferenceScreen;
import de.KnollFrank.lib.settingssearch.common.Keyboard;
import de.KnollFrank.lib.settingssearch.common.task.AsyncTaskWithProgressUpdateListeners;
import de.KnollFrank.lib.settingssearch.common.task.AsyncTaskWithProgressUpdateListenersAndProgressContainer;
import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunner;
import de.KnollFrank.lib.settingssearch.common.task.Tasks;
import de.KnollFrank.lib.settingssearch.provider.IncludePreferenceInSearchResultsPredicate;
import de.KnollFrank.lib.settingssearch.results.recyclerview.FragmentContainerViewAdder;
import de.KnollFrank.lib.settingssearch.results.recyclerview.SearchResultsFragment;
import de.KnollFrank.lib.settingssearch.search.progress.ProgressDisplayer;
import de.KnollFrank.lib.settingssearch.search.progress.ProgressUpdateListener;
import de.KnollFrank.lib.settingssearch.search.ui.ProgressContainerUI;
import de.KnollFrank.lib.settingssearch.search.ui.SearchPreferenceFragmentUI;
import de.KnollFrank.lib.settingssearch.search.ui.SearchPreferenceFragmentUIBinding;

// FK-TODO: let users of this library extend this class for their custom UI instead of using SearchPreferenceFragmentUI?
public class SearchPreferenceFragment extends Fragment {

    private static final @IdRes int DUMMY_FRAGMENT_CONTAINER_VIEW = View.generateViewId();

    private final Optional<String> queryHint;
    private final IncludePreferenceInSearchResultsPredicate includePreferenceInSearchResultsPredicate;
    private final MergedPreferenceScreenFactory mergedPreferenceScreenFactory;
    private final OnUiThreadRunner onUiThreadRunner;
    private final Supplier<Optional<AsyncTaskWithProgressUpdateListeners<?>>> createSearchDatabaseTaskSupplier;
    private final SearchPreferenceFragmentUI searchPreferenceFragmentUI;
    private final Consumer<MergedPreferenceScreen> onMergedPreferenceScreenAvailable;
    private SearchPreferenceFragmentUIBinding searchPreferenceFragmentUIBinding;
    private AsyncTaskWithProgressUpdateListenersAndProgressContainer<MergedPreferenceScreen> getMergedPreferenceScreenAndShowSearchResultsTask;

    public SearchPreferenceFragment(final Optional<String> queryHint,
                                    final IncludePreferenceInSearchResultsPredicate includePreferenceInSearchResultsPredicate,
                                    final MergedPreferenceScreenFactory mergedPreferenceScreenFactory,
                                    final OnUiThreadRunner onUiThreadRunner,
                                    final Supplier<Optional<AsyncTaskWithProgressUpdateListeners<?>>> createSearchDatabaseTaskSupplier,
                                    final SearchPreferenceFragmentUI searchPreferenceFragmentUI,
                                    final Consumer<MergedPreferenceScreen> onMergedPreferenceScreenAvailable) {
        this.queryHint = queryHint;
        this.includePreferenceInSearchResultsPredicate = includePreferenceInSearchResultsPredicate;
        this.mergedPreferenceScreenFactory = mergedPreferenceScreenFactory;
        this.onUiThreadRunner = onUiThreadRunner;
        this.createSearchDatabaseTaskSupplier = createSearchDatabaseTaskSupplier;
        this.searchPreferenceFragmentUI = searchPreferenceFragmentUI;
        this.onMergedPreferenceScreenAvailable = onMergedPreferenceScreenAvailable;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        searchPreferenceFragmentUIBinding =
                new SearchPreferenceFragmentUIBinding(
                        searchPreferenceFragmentUI,
                        inflater.inflate(
                                searchPreferenceFragmentUI.getRootViewId(),
                                container,
                                false));
        final View view = searchPreferenceFragmentUIBinding.getRoot();
        FragmentContainerViewAdder.addInvisibleFragmentContainerViewWithIdToParent(
                (ViewGroup) view,
                DUMMY_FRAGMENT_CONTAINER_VIEW);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        final ProgressDisplayer progressDisplayer = createProgressDisplayer();
        getMergedPreferenceScreenAndShowSearchResultsTask = createGetMergedPreferenceScreenAndShowSearchResultsTask(progressDisplayer);
        Tasks.asynchronouslyWaitForTask1ThenExecuteTask2(
                createSearchDatabaseTaskSupplier.get(),
                progressDisplayer,
                getMergedPreferenceScreenAndShowSearchResultsTask);
    }

    @Override
    public void onPause() {
        super.onPause();
        getMergedPreferenceScreenAndShowSearchResultsTask.cancel(false);
    }

    private ProgressDisplayer createProgressDisplayer() {
        final ProgressContainerUI progressContainerUI = searchPreferenceFragmentUIBinding.getProgressContainerUI();
        return new ProgressDisplayer(
                progressContainerUI.getRoot(),
                progressContainerUI.getProgressText());
    }

    private AsyncTaskWithProgressUpdateListenersAndProgressContainer<MergedPreferenceScreen> createGetMergedPreferenceScreenAndShowSearchResultsTask(
            final ProgressUpdateListener progressUpdateListener) {
        final var getMergedPreferenceScreenAndShowSearchResultsTask =
                new AsyncTaskWithProgressUpdateListenersAndProgressContainer<>(
                        this::getMergedPreferenceScreen,
                        mergedPreferenceScreen -> {
                            showSearchResultsFragment(
                                    mergedPreferenceScreen.searchResultsDisplayer().getSearchResultsFragment(),
                                    searchResultsPreferenceFragment -> configureSearchView(mergedPreferenceScreen));
                            onMergedPreferenceScreenAvailable.accept(mergedPreferenceScreen);
                        },
                        searchPreferenceFragmentUIBinding.getProgressContainerUI().getRoot(),
                        onUiThreadRunner);
        getMergedPreferenceScreenAndShowSearchResultsTask.addProgressUpdateListener(progressUpdateListener);
        return getMergedPreferenceScreenAndShowSearchResultsTask;
    }

    private MergedPreferenceScreen getMergedPreferenceScreen(final ProgressUpdateListener progressUpdateListener) {
        return mergedPreferenceScreenFactory.getMergedPreferenceScreen(
                getChildFragmentManager(),
                progressUpdateListener,
                DUMMY_FRAGMENT_CONTAINER_VIEW);
    }

    private void showSearchResultsFragment(final SearchResultsFragment searchResultsFragment,
                                           final Consumer<SearchResultsFragment> onFragmentStarted) {
        showFragment(
                searchResultsFragment,
                onFragmentStarted,
                false,
                searchPreferenceFragmentUIBinding.getSearchResultsFragmentContainerView().getId(),
                Optional.empty(),
                getChildFragmentManager());
    }

    private void configureSearchView(final MergedPreferenceScreen mergedPreferenceScreen) {
        final SearchView searchView = searchPreferenceFragmentUIBinding.getSearchView();
        final SearchAndDisplay searchAndDisplay =
                new SearchAndDisplay(
                        new PreferenceSearcher(
                                mergedPreferenceScreen.searchablePreferenceDAO(),
                                includePreferenceInSearchResultsPredicate),
                        mergedPreferenceScreen.searchResultsDisplayer());
        SearchViewConfigurer.configureSearchView(searchView, queryHint, searchAndDisplay);
        selectSearchView(searchView);
        searchView.setQuery(searchView.getQuery(), true);
        searchPreferenceFragmentUI.onSearchReady(
                getView(),
                new SearchForQueryAndDisplayResultsCommand(searchAndDisplay, searchView));
    }

    private void selectSearchView(final SearchView searchView) {
        searchView.requestFocus();
        Keyboard.showKeyboard(getActivity(), searchView);
    }
}
