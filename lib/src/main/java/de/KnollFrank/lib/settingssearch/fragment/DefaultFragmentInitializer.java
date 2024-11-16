package de.KnollFrank.lib.settingssearch.fragment;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunner;

public class DefaultFragmentInitializer implements FragmentInitializer, PreferenceDialogs {

    private final FragmentManager fragmentManager;
    private final @IdRes int containerViewId;
    private final OnUiThreadRunner onUiThreadRunner;

    public DefaultFragmentInitializer(final FragmentManager fragmentManager,
                                      final @IdRes int containerViewId,
                                      final OnUiThreadRunner onUiThreadRunner) {
        this.fragmentManager = fragmentManager;
        this.containerViewId = containerViewId;
        this.onUiThreadRunner = onUiThreadRunner;
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
        onUiThreadRunner.runBlockingOnUiThread(
                () -> {
                    this
                            .fragmentManager
                            .beginTransaction()
                            .add(this.containerViewId, fragment)
                            .commitNow();
                    return null;
                });
    }

    private void remove(final Fragment fragment) {
        onUiThreadRunner.runBlockingOnUiThread(
                () -> {
                    this
                            .fragmentManager
                            .beginTransaction()
                            .remove(fragment)
                            .commitNow();
                    return null;
                });
    }
}
