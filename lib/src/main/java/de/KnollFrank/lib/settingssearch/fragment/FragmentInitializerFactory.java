package de.KnollFrank.lib.settingssearch.fragment;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunner;
import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunnerFactory;

public class FragmentInitializerFactory {

    public static FragmentInitializer createFragmentInitializer(final FragmentManager fragmentManager,
                                                                final @IdRes int containerViewId,
                                                                final OnUiThreadRunner onUiThreadRunner) {
        return new FragmentInitializer(new FragmentAdderRemover(fragmentManager, containerViewId, onUiThreadRunner));
    }

    public static FragmentInitializer createFragmentInitializer(final FragmentActivity fragmentActivity,
                                                                final @IdRes int containerViewId) {
        return createFragmentInitializer(
                fragmentActivity.getSupportFragmentManager(),
                containerViewId,
                OnUiThreadRunnerFactory.fromActivity(fragmentActivity));
    }
}
