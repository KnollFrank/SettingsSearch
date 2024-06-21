package de.KnollFrank.lib.preferencesearch.fragment;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class FragmentInitializer {

    private final FragmentManager fragmentManager;
    private final @IdRes int containerResId;

    public FragmentInitializer(final FragmentManager fragmentManager, final @IdRes int containerResId) {
        this.fragmentManager = fragmentManager;
        this.containerResId = containerResId;
    }

    public void initialize(final Fragment fragment) {
        this
                .fragmentManager
                .beginTransaction()
                .replace(this.containerResId, fragment)
                .commitNow();
    }
}
