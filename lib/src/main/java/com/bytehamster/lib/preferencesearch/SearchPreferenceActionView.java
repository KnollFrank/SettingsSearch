package com.bytehamster.lib.preferencesearch;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

public class SearchPreferenceActionView extends SearchView {

    protected final SearchConfiguration searchConfiguration = new SearchConfiguration();
    protected SearchPreferenceFragment searchPreferenceFragment;
    protected FragmentActivity activity;

    public SearchPreferenceActionView(final Context context) {
        super(context);
        initView();
    }

    public SearchPreferenceActionView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SearchPreferenceActionView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public SearchConfiguration getSearchConfiguration() {
        return searchConfiguration;
    }

    public void setActivity(final FragmentActivity activity) {
        searchConfiguration.setActivity(activity);
        this.activity = activity;
    }

    /**
     * Hides the search fragment
     *
     * @return true if it was hidden, so the calling activity should not go back itself.
     */
    public boolean cancelSearch() {
        setQuery("", false);

        boolean didSomething = false;
        if (!isIconified()) {
            setIconified(true);
            didSomething = true;
        }
        if (searchPreferenceFragment != null && searchPreferenceFragment.isVisible()) {
            removeFragment();
            didSomething = true;
        }
        return didSomething;
    }

    private void removeFragment() {
        if (searchPreferenceFragment.isVisible()) {
            final FragmentManager fragmentManager = activity.getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .remove(searchPreferenceFragment)
                    .commit();
            fragmentManager.popBackStack(
                    SearchPreferenceFragment.TAG,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    private void initView() {
        searchConfiguration.setSearchBarEnabled(false);
        setOnQueryTextListener(
                new OnQueryTextListener() {

                    @Override
                    public boolean onQueryTextSubmit(final String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(final String newText) {
                        if (searchPreferenceFragment != null) {
                            searchPreferenceFragment.setSearchTerm(newText);
                        }
                        return true;
                    }
                });
        setOnQueryTextFocusChangeListener(
                (v, hasFocus) -> {
                    if (hasFocus && (searchPreferenceFragment == null || !searchPreferenceFragment.isVisible())) {
                        searchPreferenceFragment = new SearchPreferenceFragments(searchConfiguration).showSearchFragment();
                        searchPreferenceFragment.setHistoryClickListener(entry -> setQuery(entry, false));
                    }
                });
    }
}
