package de.KnollFrank.lib.settingssearch.fragment;

import android.content.Context;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentManager.FragmentLifecycleCallbacks;
import androidx.fragment.app.FragmentTransaction;

import java.util.Optional;
import java.util.function.Consumer;

import de.KnollFrank.lib.settingssearch.PreferenceOfHostOfActivity;
import de.KnollFrank.lib.settingssearch.fragment.factory.FragmentFactoryAndInitializerRegistry;

public class Fragments implements InstantiateAndInitializeFragment {

    private final FragmentFactoryAndInitializerRegistry fragmentFactoryAndInitializerRegistry;
    private final Context context;

    public Fragments(final FragmentFactoryAndInitializerRegistry fragmentFactoryAndInitializerRegistry,
                     final Context context) {
        this.fragmentFactoryAndInitializerRegistry = fragmentFactoryAndInitializerRegistry;
        this.context = context;
    }

    @Override
    public <T extends Fragment> T instantiateAndInitializeFragment(final Class<T> fragmentClass,
                                                                   final Optional<PreferenceOfHostOfActivity> src) {
        return fragmentFactoryAndInitializerRegistry.instantiateAndInitializeFragment(fragmentClass, src, context, this);
    }

    public static <T extends Fragment> void showFragment(final T fragment,
                                                         final Consumer<T> onFragmentShown,
                                                         final boolean addToBackStack,
                                                         final @IdRes int containerViewId,
                                                         final Optional<String> tag,
                                                         final FragmentManager fragmentManager) {
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setReorderingAllowed(true);
        transaction.replace(containerViewId, fragment, tag.orElse(null));
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        executeOnceOnFragmentResumed(fragment, onFragmentShown, fragmentManager);
        transaction.commit();
    }

    public static void hideFragment(final Fragment fragment,
                                    final FragmentManager fragmentManager) {
        fragmentManager
                .beginTransaction()
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .remove(fragment)
                .commit();
    }

    private static <T extends Fragment> void executeOnceOnFragmentResumed(
            final T fragment,
            final Consumer<T> onFragmentResumed,
            final FragmentManager fragmentManager) {
        fragmentManager.registerFragmentLifecycleCallbacks(
                new FragmentLifecycleCallbacks() {

                    @Override
                    public void onFragmentResumed(@NonNull final FragmentManager fragmentManager,
                                                  @NonNull final Fragment _fragment) {
                        if (_fragment == fragment) {
                            fragmentManager.unregisterFragmentLifecycleCallbacks(this);
                            onFragmentResumed.accept(fragment);
                        }
                    }
                },
                false);
    }
}
