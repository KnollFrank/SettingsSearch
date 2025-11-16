package de.KnollFrank.lib.settingssearch.fragment;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceFragmentCompat;

import java.util.function.BiConsumer;

import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunner;
import de.KnollFrank.lib.settingssearch.provider.PreferenceSearchablePredicate;

class FragmentAdderRemover {

    private final FragmentManager fragmentManager;
    private final @IdRes int containerViewId;
    private final OnUiThreadRunner onUiThreadRunner;
    private final PreferenceSearchablePredicate preferenceSearchablePredicate;

    public FragmentAdderRemover(final FragmentManager fragmentManager,
                                final @IdRes int containerViewId,
                                final OnUiThreadRunner onUiThreadRunner,
                                final PreferenceSearchablePredicate preferenceSearchablePredicate) {
        this.fragmentManager = fragmentManager;
        this.containerViewId = containerViewId;
        this.onUiThreadRunner = onUiThreadRunner;
        this.preferenceSearchablePredicate = preferenceSearchablePredicate;
    }

    public void add(final Fragment fragment) {
        if (fragment instanceof final PreferenceFragmentCompat preferenceFragment) {
            removeNonSearchablePreferencesFromPreferenceScreenOfPreferenceFragment(preferenceFragment);
        }
        executeOperationOnFragment(
                (_fragment, fragmentTransaction) -> fragmentTransaction.add(containerViewId, _fragment),
                fragment);
    }

    public void remove(final Fragment fragment) {
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

    private void removeNonSearchablePreferencesFromPreferenceScreenOfPreferenceFragment(final PreferenceFragmentCompat preferenceFragment) {
        fragmentManager.registerFragmentLifecycleCallbacks(
                new FragmentManager.FragmentLifecycleCallbacks() {

                    @Override
                    public void onFragmentResumed(@NonNull final FragmentManager fragmentManager,
                                                  @NonNull final Fragment fragment) {
                        if (fragment == preferenceFragment) {
                            PreferenceScreenAdaptor.removeNonSearchablePreferencesFromPreferenceScreenOfPreferenceFragment(preferenceFragment, preferenceSearchablePredicate);
                        }
                    }

                    @Override
                    public void onFragmentDestroyed(@NonNull final FragmentManager fragmentManager,
                                                    @NonNull final Fragment fragment) {
                        if (fragment == preferenceFragment) {
                            fragmentManager.unregisterFragmentLifecycleCallbacks(this);
                        }
                    }
                },
                false);
    }
}
