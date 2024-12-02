package de.KnollFrank.lib.settingssearch.search;

import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.fragment.app.FragmentContainerView;

public interface SearchPreferenceFragmentUI {

    @LayoutRes
    int getRootViewId();

    SearchView getSearchView(View rootView);

    FragmentContainerView getSearchResultsFragmentContainerView(View rootView);

    // FK-TODO: embed getProgressContainer() and getProgressText() in a ProgressContainerUI interface
    View getProgressContainer(View rootView);

    TextView getProgressText(View progressContainer);
}
