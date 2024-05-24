package com.bytehamster.preferencesearch.multiplePreferenceScreens;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bytehamster.lib.preferencesearch.SearchPreferenceResult;
import com.bytehamster.lib.preferencesearch.SearchPreferenceResultListener;

public class MultiplePreferenceScreensExample extends AppCompatActivity implements SearchPreferenceResultListener {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Navigation.show(new PrefsFragmentFirst(), false, getSupportFragmentManager());
    }

    @Override
    public void onSearchResultClicked(final SearchPreferenceResult result) {
        Navigation.navigatePathAndHighlightPreference(
                Navigation.getNameOfContainingFragment(result.getResourceFile()),
                result.getKey(),
                true,
                this);
    }
}
