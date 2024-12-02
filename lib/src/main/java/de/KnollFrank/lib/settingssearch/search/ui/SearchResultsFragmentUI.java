package de.KnollFrank.lib.settingssearch.search.ui;

import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;

public interface SearchResultsFragmentUI {

    @LayoutRes
    int getRootViewId();

    RecyclerView getSearchResultsView(View rootView);
}
