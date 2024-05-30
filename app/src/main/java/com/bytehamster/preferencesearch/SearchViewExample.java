package com.bytehamster.preferencesearch;


import static com.bytehamster.lib.preferencesearch.PreferenceFragments.getPreferenceFragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bytehamster.lib.preferencesearch.BaseSearchPreferenceFragment;
import com.bytehamster.lib.preferencesearch.Navigation;
import com.bytehamster.lib.preferencesearch.SearchConfiguration;
import com.bytehamster.lib.preferencesearch.SearchPreferenceActionView;
import com.bytehamster.lib.preferencesearch.SearchPreferenceResult;
import com.bytehamster.lib.preferencesearch.SearchPreferenceResultListener;
import com.bytehamster.lib.preferencesearch.ui.RevealAnimationSetting;

public class SearchViewExample extends AppCompatActivity implements SearchPreferenceResultListener {

    private static final String KEY_SEARCH_QUERY = "search_query";
    private static final String KEY_SEARCH_ENABLED = "search_enabled";
    private SearchPreferenceActionView searchPreferenceActionView;
    private MenuItem searchPreferenceMenuItem;
    private String searchQuery;
    private boolean searchEnabled;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            searchQuery = savedInstanceState.getString(KEY_SEARCH_QUERY);
            searchEnabled = savedInstanceState.getBoolean(KEY_SEARCH_ENABLED);
        }
        Navigation.show(
                new PrefsFragment(),
                false,
                getSupportFragmentManager(),
                android.R.id.content);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        searchPreferenceMenuItem = menu.findItem(R.id.search);
        searchPreferenceActionView = (SearchPreferenceActionView) searchPreferenceMenuItem.getActionView();
        searchPreferenceActionView.setActivity(this);
        configure(searchPreferenceActionView.getSearchConfiguration());
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
        Navigation.navigatePathAndHighlightPreference(
                result.getPreferenceFragmentClass().getName(),
                result.getKey(),
                true,
                this,
                android.R.id.content);
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
            findPreference("searchPreference").setVisible(false);
        }
    }

    private void configure(final SearchConfiguration searchConfiguration) {
        searchConfiguration.setPreferenceFragmentsSupplier(() ->
                getPreferenceFragments(
                        new PrefsFragment(),
                        this,
                        android.R.id.content));
        searchConfiguration.setBreadcrumbsEnabled(true);
        searchConfiguration.setFuzzySearchEnabled(false);
        searchConfiguration.setRevealAnimationSetting(
                new RevealAnimationSetting(
                        findViewById(android.R.id.content).getWidth() - getSupportActionBar().getHeight() / 2,
                        -getSupportActionBar().getHeight() / 2,
                        findViewById(android.R.id.content).getWidth(),
                        findViewById(android.R.id.content).getHeight(),
                        getResources().getColor(R.color.colorPrimary)));
    }
}
