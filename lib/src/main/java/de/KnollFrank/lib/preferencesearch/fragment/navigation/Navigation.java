package de.KnollFrank.lib.preferencesearch.fragment.navigation;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import de.KnollFrank.lib.preferencesearch.fragment.Fragments;

public class Navigation {

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

    public static void show(final Fragment fragment,
                            final boolean addToBackStack,
                            final FragmentManager fragmentManager,
                            final @IdRes int containerViewId,
                            final Runnable onFragmentStarted) {
        Fragments.executeOnceOnFragmentStarted(fragment, onFragmentStarted, fragmentManager);
        show(fragment, addToBackStack, fragmentManager, containerViewId, Commit.COMMIT_ASYNC);
    }
}
