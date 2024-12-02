package de.KnollFrank.lib.settingssearch.search.ui;

import android.view.View;
import android.widget.SearchView;

import androidx.annotation.LayoutRes;
import androidx.fragment.app.FragmentContainerView;

public interface SearchPreferenceFragmentUI {

    @LayoutRes
    int getRootViewId();

    SearchView getSearchView(View rootView);

    FragmentContainerView getSearchResultsFragmentContainerView(View rootView);

    ProgressContainerUI getProgressContainerUI();
}
