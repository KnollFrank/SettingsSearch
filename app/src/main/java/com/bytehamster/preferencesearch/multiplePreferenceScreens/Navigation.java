package com.bytehamster.preferencesearch.multiplePreferenceScreens;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

class Navigation {

    public static final String KEY_OF_PREFERENCE_2_HIGHLIGHT = "keyOfPreference2Highlight";

    public static void navigatePathAndHighlightPreference(final String fragment,
                                                          final String key,
                                                          final boolean addToBackStack,
                                                          final FragmentActivity fragmentActivity) {
        show(
                Fragment.instantiate(fragmentActivity, fragment, createArguments(key)),
                addToBackStack,
                fragmentActivity.getSupportFragmentManager());
    }

    public static void show(final Fragment fragment,
                            final boolean addToBackStack,
                            final FragmentManager fragmentManager) {
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(android.R.id.content, fragment);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack("fragment");
        }
        fragmentTransaction.commit();
    }

    private static Bundle createArguments(final String keyOfPreference2Highlight) {
        final Bundle arguments = new Bundle();
        arguments.putString(KEY_OF_PREFERENCE_2_HIGHLIGHT, keyOfPreference2Highlight);
        return arguments;
    }
}
