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
import de.KnollFrank.lib.settingssearch.R;
import de.KnollFrank.lib.settingssearch.client.SearchConfiguration;
import de.KnollFrank.lib.settingssearch.common.Keyboard;
import de.KnollFrank.lib.settingssearch.common.task.LongRunningTask;
import de.KnollFrank.lib.settingssearch.common.task.LongRunningTaskWithProgressContainer;
import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunner;
import de.KnollFrank.lib.settingssearch.common.task.Tasks;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.MergedPreferenceScreenData;
import de.KnollFrank.lib.settingssearch.provider.IncludePreferenceInSearchResultsPredicate;
import de.KnollFrank.lib.settingssearch.results.recyclerview.FragmentContainerViewAdder;
import de.KnollFrank.lib.settingssearch.results.recyclerview.SearchResultsFragment;
import de.KnollFrank.lib.settingssearch.search.progress.OnUiThreadProgressDisplayer;
import de.KnollFrank.lib.settingssearch.search.progress.ProgressDisplayerFactory;
import de.KnollFrank.lib.settingssearch.search.progress.ProgressUpdateListener;

public class SearchPreferenceFragment extends Fragment {

    private static final @IdRes int DUMMY_FRAGMENT_CONTAINER_VIEW = View.generateViewId();

    private final SearchConfiguration searchConfiguration;
    private final IncludePreferenceInSearchResultsPredicate includePreferenceInSearchResultsPredicate;
    private final MergedPreferenceScreenFactory mergedPreferenceScreenFactory;
    private final OnUiThreadRunner onUiThreadRunner;
    private final Supplier<Optional<LongRunningTask<MergedPreferenceScreenData>>> getCreateSearchDatabaseTask;

    public SearchPreferenceFragment(final SearchConfiguration searchConfiguration,
                                    final IncludePreferenceInSearchResultsPredicate includePreferenceInSearchResultsPredicate,
                                    final MergedPreferenceScreenFactory mergedPreferenceScreenFactory,
                                    final OnUiThreadRunner onUiThreadRunner,
                                    final Supplier<Optional<LongRunningTask<MergedPreferenceScreenData>>> getCreateSearchDatabaseTask) {
        super(R.layout.searchpreference_fragment);
        this.searchConfiguration = searchConfiguration;
        this.includePreferenceInSearchResultsPredicate = includePreferenceInSearchResultsPredicate;
        this.mergedPreferenceScreenFactory = mergedPreferenceScreenFactory;
        this.onUiThreadRunner = onUiThreadRunner;
        this.getCreateSearchDatabaseTask = getCreateSearchDatabaseTask;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.searchpreference_fragment, container, false);
        FragmentContainerViewAdder.addInvisibleFragmentContainerViewWithIdToParent(
                (ViewGroup) view,
                DUMMY_FRAGMENT_CONTAINER_VIEW);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        final OnUiThreadProgressDisplayer progressDisplayer =
                ProgressDisplayerFactory.createOnUiThreadProgressDisplayer(
                        requireView().findViewById(R.id.progressContainer),
                        onUiThreadRunner);
        Tasks.asynchronouslyWaitForTask1ThenExecuteTask2(
                getCreateSearchDatabaseTask.get(),
                progressDisplayer,
                createGetMergedPreferenceScreenAndShowSearchResultsTask(progressDisplayer));
    }

    private LongRunningTaskWithProgressContainer<MergedPreferenceScreen> createGetMergedPreferenceScreenAndShowSearchResultsTask(
            final ProgressUpdateListener progressUpdateListener) {
        final LongRunningTaskWithProgressContainer<MergedPreferenceScreen> getMergedPreferenceScreenAndShowSearchResultsTask =
                new LongRunningTaskWithProgressContainer<>(
                        this::getMergedPreferenceScreen,
                        mergedPreferenceScreen ->
                                showSearchResultsFragment(
                                        mergedPreferenceScreen.searchResultsDisplayer().getSearchResultsFragment(),
                                        searchResultsPreferenceFragment -> configureSearchView(mergedPreferenceScreen)),
                        requireView().findViewById(R.id.progressContainer),
                        onUiThreadRunner);
        getMergedPreferenceScreenAndShowSearchResultsTask.addProgressUpdateListener(progressUpdateListener);
        return getMergedPreferenceScreenAndShowSearchResultsTask;
    }

    private MergedPreferenceScreen getMergedPreferenceScreen(final ProgressUpdateListener progressUpdateListener) {
        return mergedPreferenceScreenFactory.getMergedPreferenceScreen(
                requireActivity().getSupportFragmentManager(),
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
                R.id.searchResultsFragmentContainerView,
                getChildFragmentManager());
    }

    private void configureSearchView(final MergedPreferenceScreen mergedPreferenceScreen) {
        // FK-TODO: im SearchPreferenceFragmentsBuilder anbieten, vom Benutzer definierte UI-Elemente SearchView und RecyclerView entgegenzunehmen, anstatt sie selbst in der Bibliothek zu erzeugen.
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
