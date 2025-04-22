package de.KnollFrank.lib.settingssearch.fragment;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunner;
import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunnerFactory;

public class PreferenceDialogsFactory {

    public static PreferenceDialogs createPreferenceDialogs(final FragmentManager fragmentManager,
                                                            final @IdRes int containerViewId,
                                                            final OnUiThreadRunner onUiThreadRunner) {
        return new PreferenceDialogs(new FragmentAdderRemover(fragmentManager, containerViewId, onUiThreadRunner));
    }

    public static PreferenceDialogs createPreferenceDialogs(final FragmentActivity fragmentActivity,
                                                            final @IdRes int containerViewId) {
        return createPreferenceDialogs(
                fragmentActivity.getSupportFragmentManager(),
                containerViewId,
                OnUiThreadRunnerFactory.fromActivity(fragmentActivity));
    }
}
