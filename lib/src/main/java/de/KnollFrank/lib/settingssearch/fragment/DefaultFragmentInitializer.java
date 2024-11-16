package de.KnollFrank.lib.settingssearch.fragment;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import de.KnollFrank.lib.settingssearch.common.task.BlockingOnUiThreadRunner;

public class DefaultFragmentInitializer implements FragmentInitializer, PreferenceDialogs {

    private final FragmentManager fragmentManager;
    private final @IdRes int containerViewId;
    private final BlockingOnUiThreadRunner blockingOnUiThreadRunner;

    public DefaultFragmentInitializer(final FragmentManager fragmentManager,
                                      final @IdRes int containerViewId,
                                      final BlockingOnUiThreadRunner blockingOnUiThreadRunner) {
        this.fragmentManager = fragmentManager;
        this.containerViewId = containerViewId;
        this.blockingOnUiThreadRunner = blockingOnUiThreadRunner;
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
        blockingOnUiThreadRunner.runOnUiThread(
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
        blockingOnUiThreadRunner.runOnUiThread(
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
