package de.KnollFrank.lib.settingssearch.client;

import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.fragment.app.FragmentContainerView;
import androidx.recyclerview.widget.RecyclerView;

import de.KnollFrank.lib.settingssearch.R;
import de.KnollFrank.lib.settingssearch.provider.IncludePreferenceInSearchResultsPredicate;
import de.KnollFrank.lib.settingssearch.provider.PrepareShow;
import de.KnollFrank.lib.settingssearch.provider.ShowPreferencePathPredicate;
import de.KnollFrank.lib.settingssearch.results.DefaultSearchResultsSorter;
import de.KnollFrank.lib.settingssearch.results.SearchResultsSorter;
import de.KnollFrank.lib.settingssearch.search.ui.ProgressContainerUI;
import de.KnollFrank.lib.settingssearch.search.ui.SearchPreferenceFragmentUI;
import de.KnollFrank.lib.settingssearch.search.ui.SearchResultsFragmentUI;

public class SearchConfigBuilder {

    private IncludePreferenceInSearchResultsPredicate includePreferenceInSearchResultsPredicate = (preference, hostOfPreference) -> true;
    private ShowPreferencePathPredicate showPreferencePathPredicate = preferencePath -> preferencePath.getPreference().isPresent();
    private PrepareShow prepareShow = preferenceFragment -> {
    };
    private SearchResultsSorter searchResultsSorter = new DefaultSearchResultsSorter();
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

    public SearchConfigBuilder withIncludePreferenceInSearchResultsPredicate(final IncludePreferenceInSearchResultsPredicate includePreferenceInSearchResultsPredicate) {
        this.includePreferenceInSearchResultsPredicate = includePreferenceInSearchResultsPredicate;
        return this;
    }

    public SearchConfigBuilder withShowPreferencePathPredicate(final ShowPreferencePathPredicate showPreferencePathPredicate) {
        this.showPreferencePathPredicate = showPreferencePathPredicate;
        return this;
    }

    public SearchConfigBuilder withPrepareShow(final PrepareShow prepareShow) {
        this.prepareShow = prepareShow;
        return this;
    }

    public SearchConfigBuilder withSearchResultsSorter(final SearchResultsSorter searchResultsSorter) {
        this.searchResultsSorter = searchResultsSorter;
        return this;
    }

    public SearchConfigBuilder withSearchPreferenceFragmentUI(final SearchPreferenceFragmentUI searchPreferenceFragmentUI) {
        this.searchPreferenceFragmentUI = searchPreferenceFragmentUI;
        return this;
    }

    public SearchConfigBuilder withSearchResultsFragmentUI(final SearchResultsFragmentUI searchResultsFragmentUI) {
        this.searchResultsFragmentUI = searchResultsFragmentUI;
        return this;
    }

    public SearchConfig build() {
        return new SearchConfig(
                includePreferenceInSearchResultsPredicate,
                showPreferencePathPredicate,
                prepareShow,
                searchResultsSorter,
                searchPreferenceFragmentUI,
                searchResultsFragmentUI);
    }
}
