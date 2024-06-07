package de.KnollFrank.preferencesearch.test;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import de.KnollFrank.lib.preferencesearch.Navigation;
import de.KnollFrank.lib.preferencesearch.SearchPreferenceResult;
import de.KnollFrank.lib.preferencesearch.SearchPreferenceResultListener;
import de.KnollFrank.preferencesearch.R;

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
