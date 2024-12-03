package de.KnollFrank.lib.settingssearch.client;

import android.content.Context;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;

import de.KnollFrank.lib.settingssearch.R;
import de.KnollFrank.lib.settingssearch.common.task.AsyncTaskWithProgressUpdateListeners;
import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunner;
import de.KnollFrank.lib.settingssearch.results.DefaultSearchResultsSorter;
import de.KnollFrank.lib.settingssearch.results.SearchResultsSorter;
import de.KnollFrank.lib.settingssearch.search.ui.ProgressContainerUI;
import de.KnollFrank.lib.settingssearch.search.ui.SearchPreferenceFragmentUI;
import de.KnollFrank.lib.settingssearch.search.ui.SearchResultsFragmentUI;

public class SearchPreferenceFragmentsBuilder {

    // FK-TODO: teile diese Felder in Subbuilder ein, z.B. ein Subbuilder für die Grapherzeugung, ein anderer Subbuilder für das UI, ...
    private final SearchConfiguration searchConfiguration;
    private final FragmentManager fragmentManager;
    private final Locale locale;
    private final OnUiThreadRunner onUiThreadRunner;
    private final Context context;
    private SearchDatabase searchDatabase = new SearchDatabaseBuilder().build();
    private Search search = new SearchBuilder().build();
    private Supplier<Optional<AsyncTaskWithProgressUpdateListeners<?>>> createSearchDatabaseTaskSupplier = Optional::empty;
    private SearchPreferenceFragmentUI searchPreferenceFragmentUI =
            new SearchPreferenceFragmentUI() {

                @Override
                public @LayoutRes int getRootViewId() {
                    return R.layout.searchpreference_fragment;
                }

                @Override
                public SearchView getSearchView(final View rootView) {
                    return rootView.findViewById(R.id.searchView);
                }

                @Override
                public FragmentContainerView getSearchResultsFragmentContainerView(final View rootView) {
                    return rootView.findViewById(R.id.searchResultsFragmentContainerView);
                }

                @Override
                public ProgressContainerUI getProgressContainerUI(final View rootView) {
                    return new ProgressContainerUI() {

                        @Override
                        public View getRoot() {
                            return rootView.findViewById(R.id.progressContainer);
                        }

                        @Override
                        public TextView getProgressText() {
                            return getRoot().findViewById(R.id.progressText);
                        }
                    };
                }
            };
    private SearchResultsFragmentUI searchResultsFragmentUI =
            new SearchResultsFragmentUI() {

                @Override
                public int getRootViewId() {
                    return R.layout.searchresults_fragment;
                }

                @Override
                public RecyclerView getSearchResultsView(View rootView) {
                    return rootView.findViewById(R.id.searchResults);
                }
            };
    private SearchResultsSorter searchResultsSorter = new DefaultSearchResultsSorter();

    protected SearchPreferenceFragmentsBuilder(final SearchConfiguration searchConfiguration,
                                               final FragmentManager fragmentManager,
                                               final Locale locale,
                                               final OnUiThreadRunner onUiThreadRunner,
                                               final Context context) {
        this.searchConfiguration = searchConfiguration;
        this.fragmentManager = fragmentManager;
        this.locale = locale;
        this.onUiThreadRunner = onUiThreadRunner;
        this.context = context;
    }

    public SearchPreferenceFragmentsBuilder withSearchDatabase(final SearchDatabase searchDatabase) {
        this.searchDatabase = searchDatabase;
        return this;
    }

    public SearchPreferenceFragmentsBuilder setSearch(final Search search) {
        this.search = search;
        return this;
    }

    public SearchPreferenceFragmentsBuilder withCreateSearchDatabaseTaskSupplier(final Supplier<Optional<AsyncTaskWithProgressUpdateListeners<?>>> createSearchDatabaseTaskSupplier) {
        this.createSearchDatabaseTaskSupplier = createSearchDatabaseTaskSupplier;
        return this;
    }

    public SearchPreferenceFragmentsBuilder withSearchPreferenceFragmentUI(final SearchPreferenceFragmentUI searchPreferenceFragmentUI) {
        this.searchPreferenceFragmentUI = searchPreferenceFragmentUI;
        return this;
    }

    public SearchPreferenceFragmentsBuilder withSearchResultsFragmentUI(final SearchResultsFragmentUI searchResultsFragmentUI) {
        this.searchResultsFragmentUI = searchResultsFragmentUI;
        return this;
    }

    public SearchPreferenceFragmentsBuilder withSearchResultsSorter(final SearchResultsSorter searchResultsSorter) {
        this.searchResultsSorter = searchResultsSorter;
        return this;
    }

    public SearchPreferenceFragments build() {
        // FK-TODO: add search as a parameter and inline 3 of the following obvious constructor params
        return new SearchPreferenceFragments(
                searchConfiguration,
                searchDatabase,
                search.includePreferenceInSearchResultsPredicate(),
                search.showPreferencePathPredicate(),
                search.prepareShow(),
                fragmentManager,
                locale,
                onUiThreadRunner,
                context,
                createSearchDatabaseTaskSupplier,
                searchPreferenceFragmentUI,
                searchResultsFragmentUI,
                searchResultsSorter);
    }
}