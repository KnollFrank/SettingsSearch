package com.bytehamster.preferencesearch;

import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.bytehamster.lib.preferencesearch.SearchConfiguration;
import com.bytehamster.lib.preferencesearch.SearchPreference;
import com.bytehamster.lib.preferencesearch.SearchPreferenceResult;
import com.bytehamster.lib.preferencesearch.SearchPreferenceResultListener;
import com.bytehamster.preferencesearch.multiplePreferenceScreens.PrefsFragmentSecond;

/**
 * This file demonstrates some additional features that might not be needed when setting it up for the first time
 */
public class EnhancedExample extends AppCompatActivity implements SearchPreferenceResultListener {
    private PrefsFragment prefsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        prefsFragment = new PrefsFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, prefsFragment)
                .commit();
    }

    @Override
    public void onSearchResultClicked(@NonNull final SearchPreferenceResult result) {
        prefsFragment = new PrefsFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, prefsFragment)
                .addToBackStack("PrefsFragment")
                .commit(); // Allow to navigate back to search

        // Allow fragment to get created
        new Handler().post(() -> prefsFragment.onSearchResultClicked(result));
    }

    public static class PrefsFragment extends PreferenceFragmentCompat {
        SearchPreference searchPreference;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.preferences);

            searchPreference = findPreference("searchPreference");
            SearchConfiguration config = searchPreference.getSearchConfiguration();
            config.setActivity((AppCompatActivity) getActivity());
            config.setFragmentContainerViewId(android.R.id.content);

            config.index(PrefsFragment.class).addBreadcrumb("Main file");
            config.index(PrefsFragmentSecond.class).addBreadcrumb("Second file");
            config.setBreadcrumbsEnabled(true);
            config.setHistoryEnabled(true);
            config.setFuzzySearchEnabled(true);
        }

        private void onSearchResultClicked(final SearchPreferenceResult result) {
            if (PrefsFragment.class.equals(result.getResourceFile())) {
                searchPreference.setVisible(false); // Do not allow to click search multiple times
                findPreference(result.getKey()).setTitle("RESULT: " + findPreference(result.getKey()).getTitle());
            } else {
                // Result was found in the other file
                getPreferenceScreen().removeAll();
                addPreferencesFromResource(R.xml.preferences2);
            }
            scrollToPreference(result.getKey());
            result.highlight(this);
        }
    }
}
