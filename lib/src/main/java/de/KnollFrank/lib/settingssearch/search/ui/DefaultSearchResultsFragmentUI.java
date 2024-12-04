package de.KnollFrank.lib.settingssearch.search.ui;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import de.KnollFrank.lib.settingssearch.R;

public class DefaultSearchResultsFragmentUI implements SearchResultsFragmentUI {

    @Override
    public int getRootViewId() {
        return R.layout.searchresults_fragment;
    }

    @Override
    public RecyclerView getSearchResultsView(View rootView) {
        return rootView.findViewById(R.id.searchResults);
    }
}
