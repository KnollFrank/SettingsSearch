package de.KnollFrank.lib.settingssearch.fragment;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentActivity;

import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunnerFactory;

public class DefaultFragmentInitializerFactory {

    public static DefaultFragmentInitializer createDefaultFragmentInitializer(final FragmentActivity fragmentActivity,
                                                                              final @IdRes int containerViewId) {
        return new DefaultFragmentInitializer(
                fragmentActivity.getSupportFragmentManager(),
                containerViewId,
                OnUiThreadRunnerFactory.fromActivity(fragmentActivity));
    }
}
