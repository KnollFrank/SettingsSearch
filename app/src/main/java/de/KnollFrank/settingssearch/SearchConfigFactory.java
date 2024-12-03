package de.KnollFrank.settingssearch;

import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.fragment.app.FragmentContainerView;
import androidx.recyclerview.widget.RecyclerView;

import de.KnollFrank.lib.settingssearch.client.SearchConfig;
import de.KnollFrank.lib.settingssearch.client.SearchConfigBuilder;
import de.KnollFrank.lib.settingssearch.search.ui.ProgressContainerUI;
import de.KnollFrank.lib.settingssearch.search.ui.SearchPreferenceFragmentUI;
import de.KnollFrank.lib.settingssearch.search.ui.SearchResultsFragmentUI;

class SearchConfigFactory {

    public static SearchConfig createSearchConfig() {
        return new SearchConfigBuilder()
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
                .build();
    }
}
