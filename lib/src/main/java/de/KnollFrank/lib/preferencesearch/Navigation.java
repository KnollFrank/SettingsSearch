package de.KnollFrank.lib.preferencesearch;

import static de.KnollFrank.lib.preferencesearch.BaseSearchPreferenceFragment.KEY_OF_PREFERENCE_2_HIGHLIGHT;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Navigation {

    public static void showPreferenceScreenAndHighlightPreference(
            final String fragmentOfPreferenceScreen,
            final String keyOfPreference2Highlight,
            final FragmentActivity fragmentActivity,
            @IdRes final int containerViewId) {
        show(
                Fragment.instantiate(fragmentActivity, fragmentOfPreferenceScreen, createArguments(keyOfPreference2Highlight)),
                false,
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
