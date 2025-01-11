package de.KnollFrank.lib.settingssearch.search.ui;

import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.fragment.app.FragmentContainerView;

import de.KnollFrank.lib.settingssearch.R;
import de.KnollFrank.lib.settingssearch.search.SearchForQueryAndDisplayResultsCommand;

public class DefaultSearchPreferenceFragmentUI implements SearchPreferenceFragmentUI {

    @Override
    public @LayoutRes int getRootViewId() {
        return R.layout.searchpreference_fragment;
    }

    @Override
    public void onSearchReady(final View rootView, final SearchForQueryAndDisplayResultsCommand searchForQueryAndDisplayResultsCommand) {
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
}
