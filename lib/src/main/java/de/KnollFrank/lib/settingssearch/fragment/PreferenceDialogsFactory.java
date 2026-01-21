package de.KnollFrank.lib.settingssearch.fragment;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunner;
import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunnerFactory;
import de.KnollFrank.lib.settingssearch.provider.PreferenceSearchablePredicate;

public class PreferenceDialogsFactory {

    private PreferenceDialogsFactory() {
    }

    public static PreferenceDialogs createPreferenceDialogs(final FragmentManager fragmentManager,
                                                            final @IdRes int containerViewId,
                                                            final OnUiThreadRunner onUiThreadRunner,
                                                            final PreferenceSearchablePredicate preferenceSearchablePredicate) {
        return new PreferenceDialogs(new FragmentAdderRemover(fragmentManager, containerViewId, onUiThreadRunner, preferenceSearchablePredicate));
    }

    public static PreferenceDialogs createPreferenceDialogs(final FragmentActivity fragmentActivity,
                                                            final @IdRes int containerViewId,
                                                            final PreferenceSearchablePredicate preferenceSearchablePredicate) {
        return createPreferenceDialogs(
                fragmentActivity.getSupportFragmentManager(),
                containerViewId,
                OnUiThreadRunnerFactory.fromActivity(fragmentActivity),
                preferenceSearchablePredicate);
    }
}
