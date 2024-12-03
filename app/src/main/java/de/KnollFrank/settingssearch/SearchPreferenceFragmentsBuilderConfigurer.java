package de.KnollFrank.settingssearch;

import android.app.Activity;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Optional;
import java.util.function.Supplier;

import de.KnollFrank.lib.settingssearch.client.SearchConfiguration;
import de.KnollFrank.lib.settingssearch.client.SearchDatabase;
import de.KnollFrank.lib.settingssearch.client.SearchPreferenceFragments;
import de.KnollFrank.lib.settingssearch.client.SearchPreferenceFragmentsBuilder;
import de.KnollFrank.lib.settingssearch.common.task.AsyncTaskWithProgressUpdateListeners;
import de.KnollFrank.lib.settingssearch.search.ui.ProgressContainerUI;
import de.KnollFrank.lib.settingssearch.search.ui.SearchPreferenceFragmentUI;
import de.KnollFrank.lib.settingssearch.search.ui.SearchResultsFragmentUI;

public class SearchPreferenceFragmentsBuilderConfigurer {

    public static SearchPreferenceFragmentsBuilder configure(
            final SearchConfiguration searchConfiguration,
            final FragmentManager fragmentManager,
            final Activity activity,
            final SearchDatabase searchDatabase,
            final Supplier<Optional<AsyncTaskWithProgressUpdateListeners<?>>> createSearchDatabaseTaskSupplier) {
        return SearchPreferenceFragments
                .builder(
                        searchConfiguration,
                        fragmentManager,
                        activity,
                        searchDatabase)
                .withSearchPreferenceFragmentUI(
                        new SearchPreferenceFragmentUI() {

                            @Override
                            public @LayoutRes int getRootViewId() {
                                return R.layout.custom_searchpreference_fragment;
                            }

                            @Override
                            public SearchView getSearchView(final View rootView) {
                                return rootView.findViewById(R.id.searchViewCustom);
                            }

                            @Override
                            public FragmentContainerView getSearchResultsFragmentContainerView(final View rootView) {
                                return rootView.findViewById(R.id.searchResultsFragmentContainerViewCustom);
                            }

                            @Override
                            public ProgressContainerUI getProgressContainerUI(View rootView) {
                                return new ProgressContainerUI() {

                                    @Override
                                    public View getRoot() {
                                        return rootView.findViewById(R.id.progressContainerCustom);
                                    }

                                    @Override
                                    public TextView getProgressText() {
                                        return getRoot().findViewById(de.KnollFrank.lib.settingssearch.R.id.progressText);
                                    }
                                };
                            }
                        })
                .withSearchResultsFragmentUI(
                        new SearchResultsFragmentUI() {

                            @Override
                            public @LayoutRes int getRootViewId() {
                                return R.layout.custom_searchresults_fragment;
                            }

                            @Override
                            public RecyclerView getSearchResultsView(final View rootView) {
                                return rootView.findViewById(R.id.searchResultsCustom);
                            }
                        })
                .withCreateSearchDatabaseTaskSupplier(createSearchDatabaseTaskSupplier);
    }
}
