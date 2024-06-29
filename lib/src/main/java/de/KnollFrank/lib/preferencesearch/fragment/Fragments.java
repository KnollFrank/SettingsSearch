package de.KnollFrank.lib.preferencesearch.fragment;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentManager.FragmentLifecycleCallbacks;

public class Fragments {

    private final FragmentFactory fragmentFactory;
    private final FragmentInitializer fragmentInitializer;
    private final Context context;

    public Fragments(final FragmentFactory fragmentFactory,
                     final FragmentInitializer fragmentInitializer,
                     final Context context) {
        this.fragmentFactory = fragmentFactory;
        this.fragmentInitializer = fragmentInitializer;
        this.context = context;
    }

    public Fragment instantiateAndInitializeFragment(final String fragmentClassName) {
        final Fragment _fragment = fragmentFactory.instantiate(fragmentClassName, context);
        fragmentInitializer.initialize(_fragment);
        return _fragment;
    }

    public static void executeOnceOnFragmentStarted(final Fragment fragment,
                                                    final Runnable onFragmentStarted,
                                                    final FragmentManager fragmentManager) {
        fragmentManager.registerFragmentLifecycleCallbacks(
                new FragmentLifecycleCallbacks() {

                    @Override
                    public void onFragmentStarted(@NonNull final FragmentManager fragmentManager,
                                                  @NonNull final Fragment _fragment) {
                        if (_fragment == fragment) {
                            fragmentManager.unregisterFragmentLifecycleCallbacks(this);
                            onFragmentStarted.run();
                        }
                    }
                },
                false);
    }
}
