package de.KnollFrank.lib.preferencesearch.fragment.navigation;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.function.Consumer;

import de.KnollFrank.lib.preferencesearch.fragment.Fragments;

public class Navigation {

    public static <T extends Fragment> void show(final T fragment,
                                                 final boolean addToBackStack,
                                                 final FragmentManager fragmentManager,
                                                 final @IdRes int containerViewId,
                                                 final Commit commit,
                                                 final Consumer<T> onFragmentStarted) {
        final FragmentTransaction fragmentTransaction =
                fragmentManager
                        .beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(containerViewId, fragment);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        Fragments.executeOnceOnFragmentStarted(fragment, onFragmentStarted, fragmentManager);
        commit.commit(fragmentTransaction);
    }
}
