package com.bytehamster.preferencesearch.test;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import com.bytehamster.lib.preferencesearch.Navigation;
import com.bytehamster.lib.preferencesearch.SearchPreferenceResult;
import com.bytehamster.lib.preferencesearch.SearchPreferenceResultListener;
import com.bytehamster.preferencesearch.R;

public class TestActivity extends AppCompatActivity implements SearchPreferenceResultListener {

    @IdRes
    public static final int FRAGMENT_CONTAINER_VIEW = R.id.fragmentContainerView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
    }

    @Override
    public void onSearchResultClicked(final SearchPreferenceResult result) {
        Navigation.showPreferenceScreenAndHighlightPreference(
                result.getPreferenceFragmentClass().getName(),
                result.getKey(),
                this,
                FRAGMENT_CONTAINER_VIEW);
    }
}
