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

public class MultiplePreferenceScreensExample extends AppCompatActivity implements SearchPreferenceResultListener {

    private PrefsFragment prefsFragment;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.prefsFragment = new PrefsFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, prefsFragment)
                .commit();
    }

    @Override
    public void onSearchResultClicked(@NonNull final SearchPreferenceResult result) {
        this.prefsFragment = new PrefsFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, prefsFragment)
                .addToBackStack("PrefsFragment")
                .commit(); // Allow to navigate back to search
        // Allow fragment to get created
        new Handler().post(() -> prefsFragment.onSearchResultClicked(result));
    }

    public static class PrefsFragment extends PreferenceFragmentCompat {

        private SearchPreference searchPreference;

        @Override
        public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
            addPreferencesFromResource(R.xml.preferences_multiple_screens);

            this.searchPreference = findPreference("searchPreference");
            final SearchConfiguration config = searchPreference.getSearchConfiguration();
            config.setActivity((AppCompatActivity) getActivity());
            config.setFragmentContainerViewId(android.R.id.content);

            config.index(R.xml.preferences_multiple_screens).addBreadcrumb("Main file");
            config.index(R.xml.preferences2).addBreadcrumb("Second file");
            config.setBreadcrumbsEnabled(true);
            config.setHistoryEnabled(true);
            config.setFuzzySearchEnabled(true);
        }

        private void onSearchResultClicked(final SearchPreferenceResult result) {
            if (result.getResourceFile() == R.xml.preferences_multiple_screens) {
                searchPreference.setVisible(false); // Do not allow to click search multiple times
                scrollToPreference(result.getKey());
                findPreference(result.getKey()).setTitle("RESULT: " + findPreference(result.getKey()).getTitle());
            } else {
                findPreference("global_settings").performClick();
                // result.highlight(this);
            }
        }
    }

    public static class PrefsFragmentSecond extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
            addPreferencesFromResource(R.xml.preferences2);
        }
    }
}
