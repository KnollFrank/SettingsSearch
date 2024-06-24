package de.KnollFrank.lib.preferencesearch;

import static de.KnollFrank.lib.preferencesearch.BaseSearchPreferenceFragment.KEY_OF_PREFERENCE_2_HIGHLIGHT;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceFragmentCompat;

// FK-TODO: move Navigation and Commit to package common.fragment.navigation
public class Navigation {

    // FK-TODO: move to caller class
    public static void showPreferenceScreenAndHighlightPreference(
            final Class<? extends PreferenceFragmentCompat> fragmentOfPreferenceScreen,
            final String keyOfPreference2Highlight,
            final FragmentActivity fragmentActivity,
            final @IdRes int containerViewId) {
        show(
                Fragment.instantiate(
                        fragmentActivity,
                        fragmentOfPreferenceScreen.getName(),
                        createArguments(keyOfPreference2Highlight)),
                true,
                fragmentActivity.getSupportFragmentManager(),
                containerViewId,
                Commit.COMMIT);
    }

    public static void show(final Fragment fragment,
                            final boolean addToBackStack,
                            final FragmentManager fragmentManager,
                            final @IdRes int containerViewId,
                            final Commit commit) {
        final FragmentTransaction fragmentTransaction =
                fragmentManager
                        .beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(containerViewId, fragment);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        commit.commit(fragmentTransaction);
    }

    private static Bundle createArguments(final String keyOfPreference2Highlight) {
        final Bundle arguments = new Bundle();
        arguments.putString(KEY_OF_PREFERENCE_2_HIGHLIGHT, keyOfPreference2Highlight);
        return arguments;
    }
}
