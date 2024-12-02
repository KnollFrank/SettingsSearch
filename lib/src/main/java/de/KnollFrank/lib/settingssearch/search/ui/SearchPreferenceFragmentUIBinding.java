package de.KnollFrank.lib.settingssearch.search.ui;

import android.view.View;
import android.widget.SearchView;

import androidx.fragment.app.FragmentContainerView;

public class SearchPreferenceFragmentUIBinding {

    private final SearchPreferenceFragmentUI searchPreferenceFragmentUI;
    private final View root;

    public SearchPreferenceFragmentUIBinding(final SearchPreferenceFragmentUI searchPreferenceFragmentUI,
                                             final View root) {
        this.searchPreferenceFragmentUI = searchPreferenceFragmentUI;
        this.root = root;
    }

    public View getRoot() {
        return root;
    }

    public SearchView getSearchView() {
        return searchPreferenceFragmentUI.getSearchView(root);
    }

    public FragmentContainerView getSearchResultsFragmentContainerView() {
        return searchPreferenceFragmentUI.getSearchResultsFragmentContainerView(root);
    }

    public ProgressContainerUI getProgressContainerUI() {
        return searchPreferenceFragmentUI.getProgressContainerUI(root);
    }
}
