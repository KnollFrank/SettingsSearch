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
import de.KnollFrank.lib.settingssearch.provider.IncludePreferenceInSearchResultsPredicate;
import de.KnollFrank.lib.settingssearch.provider.PrepareShow;
import de.KnollFrank.lib.settingssearch.provider.ShowPreferencePathPredicate;
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
    private IncludePreferenceInSearchResultsPredicate includePreferenceInSearchResultsPredicate = (preference, hostOfPreference) -> true;
    private ShowPreferencePathPredicate showPreferencePathPredicate = preferencePath -> preferencePath.getPreference().isPresent();
    private PrepareShow prepareShow = preferenceFragment -> {
    };
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

    // FK-TODO: move constructor params to build() method
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

    public SearchPreferenceFragmentsBuilder withIncludePreferenceInSearchResultsPredicate(final IncludePreferenceInSearchResultsPredicate includePreferenceInSearchResultsPredicate) {
        this.includePreferenceInSearchResultsPredicate = includePreferenceInSearchResultsPredicate;
        return this;
    }

    public SearchPreferenceFragmentsBuilder withShowPreferencePathPredicate(final ShowPreferencePathPredicate showPreferencePathPredicate) {
        this.showPreferencePathPredicate = showPreferencePathPredicate;
        return this;
    }

    public SearchPreferenceFragmentsBuilder withPrepareShow(final PrepareShow prepareShow) {
        this.prepareShow = prepareShow;
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
        return new SearchPreferenceFragments(
                searchConfiguration,
                searchDatabase,
                includePreferenceInSearchResultsPredicate,
                showPreferencePathPredicate,
                prepareShow,
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