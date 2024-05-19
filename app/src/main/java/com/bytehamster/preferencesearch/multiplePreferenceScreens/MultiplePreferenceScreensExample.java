package com.bytehamster.preferencesearch.multiplePreferenceScreens;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bytehamster.lib.preferencesearch.SearchPreferenceResult;
import com.bytehamster.lib.preferencesearch.SearchPreferenceResultListener;
import com.bytehamster.preferencesearch.R;

public class MultiplePreferenceScreensExample extends AppCompatActivity implements SearchPreferenceResultListener {

    public static final String KEY_OF_PREFERENCE_2_HIGHLIGHT = "KEY_OF_PREFERENCE_2_HIGHLIGHT";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        show(new PrefsFragment(), false);
    }

    @Override
    public void onSearchResultClicked(@NonNull final SearchPreferenceResult result) {
        show(
                createFragment(
                        result.getResourceFile(),
                        createArguments(result.getKey())),
                true);
    }

    private static Fragment createFragment(@IdRes final int resourceFile, final Bundle arguments) {
        final Fragment fragment = createFragment(resourceFile);
        fragment.setArguments(arguments);
        return fragment;
    }

    private static Fragment createFragment(@IdRes final int resourceFile) {
        switch (resourceFile) {
            case R.xml.preferences_multiple_screens:
                return new PrefsFragment();
            case R.xml.preferences2:
                return new PrefsFragmentSecond();
            default:
                throw new IllegalArgumentException();
        }
    }

    private static Bundle createArguments(final String keyOfPreference2Highlight) {
        final Bundle arguments = new Bundle();
        arguments.putString(MultiplePreferenceScreensExample.KEY_OF_PREFERENCE_2_HIGHLIGHT, keyOfPreference2Highlight);
        return arguments;
    }

    private void show(final Fragment fragment, final boolean addToBackStack) {
        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(android.R.id.content, fragment);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack("fragment");
        }
        fragmentTransaction.commit();
    }
}
