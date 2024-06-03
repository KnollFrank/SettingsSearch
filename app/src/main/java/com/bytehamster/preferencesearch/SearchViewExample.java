package com.bytehamster.preferencesearch;


import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.bytehamster.lib.preferencesearch.BaseSearchPreferenceFragment;
import com.bytehamster.lib.preferencesearch.Navigation;
import com.bytehamster.lib.preferencesearch.PreferenceFragments;
import com.bytehamster.lib.preferencesearch.SearchConfiguration;
import com.bytehamster.lib.preferencesearch.SearchPreferenceActionView;
import com.bytehamster.lib.preferencesearch.SearchPreferenceResult;
import com.bytehamster.lib.preferencesearch.SearchPreferenceResultListener;
import com.bytehamster.lib.preferencesearch.common.UIUtils;
import com.bytehamster.lib.preferencesearch.ui.RevealAnimationSetting;

public class SearchViewExample extends AppCompatActivity implements SearchPreferenceResultListener {

    @IdRes
    private static final int FRAGMENT_CONTAINER_VIEW = R.id.fragmentContainerView;

    private static final String KEY_SEARCH_QUERY = "search_query";
    private static final String KEY_SEARCH_ENABLED = "search_enabled";
    private SearchPreferenceActionView searchPreferenceActionView;
    private MenuItem searchPreferenceMenuItem;
    private String searchQuery;
    private boolean searchEnabled;
    @IdRes
    private int dummyFragmentContainerViewId = View.NO_ID;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _setContentView(R.layout.multiple_preference_screens_example);
        if (savedInstanceState != null) {
            searchQuery = savedInstanceState.getString(KEY_SEARCH_QUERY);
            searchEnabled = savedInstanceState.getBoolean(KEY_SEARCH_ENABLED);
        }
        if (savedInstanceState == null) {
            Navigation.show(
                    new PrefsFragment(),
                    false,
                    getSupportFragmentManager(),
                    FRAGMENT_CONTAINER_VIEW);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        searchPreferenceMenuItem = menu.findItem(R.id.search);
        searchPreferenceActionView = (SearchPreferenceActionView) searchPreferenceMenuItem.getActionView();
        searchPreferenceActionView.setActivity(this);
        configure(searchPreferenceActionView.getSearchConfiguration(), new PrefsFragment());
        searchPreferenceMenuItem.setOnActionExpandListener(
                new MenuItem.OnActionExpandListener() {

                    @Override
                    public boolean onMenuItemActionCollapse(final MenuItem item) {
                        searchPreferenceActionView.cancelSearch();
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionExpand(final MenuItem item) {
                        return true;
                    }
                });
        if (searchEnabled) {
            new Handler().post(() -> {
                // If we do not use a handler here, it will not be possible
                // to use the menuItem after dismissing the searchView
                searchPreferenceMenuItem.expandActionView();
                searchPreferenceActionView.setQuery(searchQuery, false);
            });
        }
        return true;
    }

    @Override
    public void onSearchResultClicked(@NonNull final SearchPreferenceResult result) {
        searchPreferenceActionView.cancelSearch();
        searchPreferenceMenuItem.collapseActionView();
        Navigation.showPreferenceScreenAndHighlightPreference(
                result.getPreferenceFragmentClass().getName(),
                result.getKey(),
                this,
                FRAGMENT_CONTAINER_VIEW);
    }

    @Override
    public void onBackPressed() {
        if (!searchPreferenceActionView.cancelSearch()) {
            super.onBackPressed();
        }
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        outState.putString(KEY_SEARCH_QUERY, searchPreferenceActionView.getQuery().toString());
        outState.putBoolean(KEY_SEARCH_ENABLED, !searchPreferenceActionView.isIconified());
        searchPreferenceActionView.cancelSearch();
        super.onSaveInstanceState(outState);
    }

    public static class PrefsFragment extends BaseSearchPreferenceFragment {

        @Override
        public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
            addPreferencesFromResource(R.xml.preferences_multiple_screens);
        }
    }

    private void _setContentView(final @LayoutRes int resource) {
        final Pair<View, Integer> contentViewAndDummyFragmentContainerViewId =
                UIUtils.createContentViewAndDummyFragmentContainerViewId(
                        resource,
                        this);
        dummyFragmentContainerViewId = contentViewAndDummyFragmentContainerViewId.second;
        setContentView(contentViewAndDummyFragmentContainerViewId.first);
    }

    private void configure(final SearchConfiguration searchConfiguration,
                           final PreferenceFragmentCompat root) {
        searchConfiguration.setFragmentContainerViewId(FRAGMENT_CONTAINER_VIEW);
        searchConfiguration.setDummyFragmentContainerViewId(dummyFragmentContainerViewId);
        searchConfiguration.setPreferenceFragments(
                PreferenceFragments.getPreferenceFragments(
                        root,
                        this,
                        dummyFragmentContainerViewId));
        searchConfiguration.setBreadcrumbsEnabled(true);
        searchConfiguration.setFuzzySearchEnabled(false);
        searchConfiguration.setHistoryEnabled(true);
        searchConfiguration.setRevealAnimationSetting(
                new RevealAnimationSetting(
                        findViewById(FRAGMENT_CONTAINER_VIEW).getWidth() - getSupportActionBar().getHeight() / 2,
                        -getSupportActionBar().getHeight() / 2,
                        findViewById(FRAGMENT_CONTAINER_VIEW).getWidth(),
                        findViewById(FRAGMENT_CONTAINER_VIEW).getHeight(),
                        getResources().getColor(R.color.colorPrimary)));
    }
}
