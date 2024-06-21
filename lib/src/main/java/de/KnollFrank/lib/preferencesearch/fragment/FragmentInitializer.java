package de.KnollFrank.lib.preferencesearch.fragment;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

class FragmentInitializer {

    private final FragmentManager fragmentManager;
    private final @IdRes int containerViewId;

    public FragmentInitializer(final FragmentManager fragmentManager, final @IdRes int containerViewId) {
        this.fragmentManager = fragmentManager;
        this.containerViewId = containerViewId;
    }

    public void initialize(final Fragment fragment) {
        this
                .fragmentManager
                .beginTransaction()
                .replace(this.containerViewId, fragment)
                .commitNow();
    }
}
