package com.bytehamster.preferencesearch.multiplePreferenceScreens;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import com.bytehamster.lib.preferencesearch.Navigation;
import com.bytehamster.lib.preferencesearch.SearchPreferenceResult;
import com.bytehamster.lib.preferencesearch.SearchPreferenceResultListener;
import com.bytehamster.preferencesearch.R;

public class MultiplePreferenceScreensExample extends AppCompatActivity implements SearchPreferenceResultListener {

    @IdRes
    public static final int FRAGMENT_CONTAINER_VIEW = R.id.fragmentContainerView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.bytehamster.preferencesearch.R.layout.multiple_preference_screens_example);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Navigation.show(
                new PrefsFragmentFirst(),
                false,
                getSupportFragmentManager(),
                FRAGMENT_CONTAINER_VIEW);
    }

    @Override
    public void onSearchResultClicked(final SearchPreferenceResult result) {
        Navigation.navigatePathAndHighlightPreference(
                result.getPreferenceFragmentClass().getName(),
                result.getKey(),
                true,
                this,
                FRAGMENT_CONTAINER_VIEW);
    }
}
