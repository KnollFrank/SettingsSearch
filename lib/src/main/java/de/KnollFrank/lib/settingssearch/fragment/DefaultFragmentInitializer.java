package de.KnollFrank.lib.settingssearch.fragment;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class DefaultFragmentInitializer implements FragmentInitializer, PreferenceDialogs {

    private final FragmentManager fragmentManager;
    private final @IdRes int containerViewId;

    public DefaultFragmentInitializer(final FragmentManager fragmentManager, final @IdRes int containerViewId) {
        this.fragmentManager = fragmentManager;
        this.containerViewId = containerViewId;
    }

    @Override
    public void initialize(final Fragment fragment) {
        add(fragment);
        remove(fragment);
    }

    @Override
    public void showPreferenceDialog(final Fragment preferenceDialog) {
        add(preferenceDialog);
    }

    @Override
    public void hidePreferenceDialog(final Fragment preferenceDialog) {
        remove(preferenceDialog);
    }

    private void add(final Fragment fragment) {
        this
                .fragmentManager
                .beginTransaction()
                .add(this.containerViewId, fragment)
                .commitNowAllowingStateLoss();
    }

    private void remove(final Fragment fragment) {
        this
                .fragmentManager
                .beginTransaction()
                .remove(fragment)
                .commitNowAllowingStateLoss();
    }
}
