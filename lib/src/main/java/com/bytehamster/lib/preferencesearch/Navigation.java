package com.bytehamster.lib.preferencesearch;

import static com.bytehamster.lib.preferencesearch.BaseSearchPreferenceFragment.KEY_OF_PREFERENCE_2_HIGHLIGHT;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Navigation {

    public static void navigatePathAndHighlightPreference(final String fragment,
                                                          final String key,
                                                          final boolean addToBackStack,
                                                          final FragmentActivity fragmentActivity,
                                                          @IdRes final int containerViewId) {
        show(
                Fragment.instantiate(fragmentActivity, fragment, createArguments(key)),
                addToBackStack,
                fragmentActivity.getSupportFragmentManager(),
                containerViewId);
    }

    public static void show(final Fragment fragment,
                            final boolean addToBackStack,
                            final FragmentManager fragmentManager,
                            @IdRes final int containerViewId) {
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (addToBackStack) {
            fragmentTransaction.addToBackStack("fragment");
        }
        fragmentTransaction
                .setReorderingAllowed(true)
                .replace(containerViewId, fragment)
                .commit();
    }

    private static Bundle createArguments(final String keyOfPreference2Highlight) {
        final Bundle arguments = new Bundle();
        arguments.putString(KEY_OF_PREFERENCE_2_HIGHLIGHT, keyOfPreference2Highlight);
        return arguments;
    }
}
