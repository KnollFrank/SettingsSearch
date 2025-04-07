package de.KnollFrank.lib.settingssearch.fragment;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.function.BiConsumer;

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
        executeOperationOnFragment(
                (_fragment, fragmentTransaction) -> fragmentTransaction.add(containerViewId, _fragment),
                fragment);
    }

    private void remove(final Fragment fragment) {
        executeOperationOnFragment(
                (_fragment, fragmentTransaction) -> fragmentTransaction.remove(_fragment),
                fragment);
    }

    private void executeOperationOnFragment(final BiConsumer<Fragment, FragmentTransaction> operation,
                                            final Fragment fragment) {
        onUiThreadRunner.runBlockingOnUiThread(
                () -> {
                    final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    operation.accept(fragment, fragmentTransaction);
                    fragmentTransaction.commitNow();
                    return null;
                });
    }
}
