package de.KnollFrank.lib.settingssearch.fragment;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunner;
import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunnerFactory;
import de.KnollFrank.lib.settingssearch.provider.PreferenceSearchablePredicate;

public class FragmentInitializerFactory {

    public static FragmentInitializer createFragmentInitializer(final FragmentManager fragmentManager,
                                                                final @IdRes int containerViewId,
                                                                final OnUiThreadRunner onUiThreadRunner,
                                                                final PreferenceSearchablePredicate preferenceSearchablePredicate) {
        return new FragmentInitializer(new FragmentAdderRemover(fragmentManager, containerViewId, onUiThreadRunner, preferenceSearchablePredicate));
    }

    public static FragmentInitializer createFragmentInitializer(final FragmentActivity fragmentActivity,
                                                                final @IdRes int containerViewId,
                                                                final PreferenceSearchablePredicate preferenceSearchablePredicate) {
        return createFragmentInitializer(
                fragmentActivity.getSupportFragmentManager(),
                containerViewId,
                OnUiThreadRunnerFactory.fromActivity(fragmentActivity),
                preferenceSearchablePredicate);
    }
}
