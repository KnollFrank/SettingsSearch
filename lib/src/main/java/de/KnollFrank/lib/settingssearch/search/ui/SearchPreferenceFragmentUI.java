package de.KnollFrank.lib.settingssearch.search.ui;

import android.view.View;
import android.widget.SearchView;

import androidx.annotation.LayoutRes;
import androidx.fragment.app.FragmentContainerView;

import de.KnollFrank.lib.settingssearch.search.SearchForQueryAndDisplayResultsCommand;

public interface SearchPreferenceFragmentUI {

    @LayoutRes
    int getRootViewId();

    SearchView getSearchView(View rootView);

    FragmentContainerView getSearchResultsFragmentContainerView(View rootView);

    ProgressContainerUI getProgressContainerUI(View rootView);

    void onSearchReady(View rootView, SearchForQueryAndDisplayResultsCommand<?> searchForQueryAndDisplayResultsCommand);
}
