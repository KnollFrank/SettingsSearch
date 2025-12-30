package de.KnollFrank.settingssearch;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.FragmentContainerView;
import androidx.recyclerview.widget.RecyclerView;

import de.KnollFrank.lib.settingssearch.client.SearchConfig;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceOfHostWithinGraph;
import de.KnollFrank.lib.settingssearch.results.SearchResultsFilter;
import de.KnollFrank.lib.settingssearch.search.SearchForQueryAndDisplayResultsCommand;
import de.KnollFrank.lib.settingssearch.search.ui.ProgressContainerUI;
import de.KnollFrank.lib.settingssearch.search.ui.SearchPreferenceFragmentUI;
import de.KnollFrank.lib.settingssearch.search.ui.SearchResultsFragmentUI;

class SearchConfigFactory {

    @VisibleForTesting
    @IdRes
    static final int SEARCH_RESULTS_VIEW_ID = R.id.searchResultsCustom;

    public static SearchConfig createSearchConfig(final @IdRes int fragmentContainerViewId,
                                                  final Context context) {
        final IgnoreSearchResultsFilter ignoreSearchResultsFilter = new IgnoreSearchResultsFilter();
        return SearchConfig
                .builder(fragmentContainerViewId, context)
                .withSearchResultsFilter(ignoreSearchResultsFilter)
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

                            @Override
                            public void onSearchReady(final View rootView, final SearchForQueryAndDisplayResultsCommand searchForQueryAndDisplayResultsCommand) {
                                configureCheckbox(
                                        rootView.findViewById(R.id.ignoreSearchResultsCheckBox),
                                        searchForQueryAndDisplayResultsCommand);
                            }

                            private void configureCheckbox(final CheckBox ignoreSearchResultsCheckBox, final SearchForQueryAndDisplayResultsCommand searchForQueryAndDisplayResultsCommand) {
                                ignoreSearchResultsCheckBox.setChecked(ignoreSearchResultsFilter.isIgnoreSearchResults());
                                ignoreSearchResultsCheckBox.setOnCheckedChangeListener(
                                        (_checkBox, isChecked) -> {
                                            ignoreSearchResultsFilter.setIgnoreSearchResults(isChecked);
                                            searchForQueryAndDisplayResultsCommand.searchForQueryAndDisplayResults();
                                        });
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
                                return rootView.findViewById(SEARCH_RESULTS_VIEW_ID);
                            }
                        })
                .build();
    }

    private static class IgnoreSearchResultsFilter implements SearchResultsFilter {

        private boolean ignoreSearchResults = false;

        @Override
        public boolean includePreferenceInSearchResults(final SearchablePreferenceOfHostWithinGraph preference) {
            return !ignoreSearchResults;
        }

        public boolean isIgnoreSearchResults() {
            return ignoreSearchResults;
        }

        public void setIgnoreSearchResults(final boolean ignoreSearchResults) {
            this.ignoreSearchResults = ignoreSearchResults;
        }
    }
}
