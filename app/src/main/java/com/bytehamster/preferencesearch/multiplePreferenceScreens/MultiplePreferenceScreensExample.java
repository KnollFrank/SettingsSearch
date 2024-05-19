package com.bytehamster.preferencesearch.multiplePreferenceScreens;

import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bytehamster.lib.preferencesearch.SearchPreferenceResult;
import com.bytehamster.lib.preferencesearch.SearchPreferenceResultListener;

public class MultiplePreferenceScreensExample extends AppCompatActivity implements SearchPreferenceResultListener {

    public static final String KEY_OF_PREFERENCE_2_HIGHLIGHT = "KEY_OF_PREFERENCE_2_HIGHLIGHT";
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
}
