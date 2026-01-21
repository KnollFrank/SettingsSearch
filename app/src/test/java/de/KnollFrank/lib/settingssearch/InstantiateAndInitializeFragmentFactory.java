package de.KnollFrank.lib.settingssearch;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactoryAndInitializer;
import de.KnollFrank.lib.settingssearch.fragment.FragmentInitializerFactory;
import de.KnollFrank.lib.settingssearch.fragment.Fragments;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;
import de.KnollFrank.lib.settingssearch.fragment.factory.FragmentFactoryAndInitializerRegistry;
import de.KnollFrank.settingssearch.test.TestActivity;

public class InstantiateAndInitializeFragmentFactory {

    private InstantiateAndInitializeFragmentFactory() {
    }

    public static InstantiateAndInitializeFragment createInstantiateAndInitializeFragment(final FragmentActivity activity) {
        return createInstantiateAndInitializeFragment(activity, new DefaultFragmentFactory());
    }

    public static InstantiateAndInitializeFragment createInstantiateAndInitializeFragment(final Fragment fragment,
                                                                                          final FragmentActivity activity) {
        return createInstantiateAndInitializeFragment(activity, createFragmentFactoryReturning(fragment));
    }

    public static Fragments createInstantiateAndInitializeFragment(final FragmentActivity activity,
                                                                   final FragmentFactory fragmentFactory) {
        return new Fragments(
                new FragmentFactoryAndInitializerRegistry(
                        new FragmentFactoryAndInitializer(
                                fragmentFactory,
                                FragmentInitializerFactory.createFragmentInitializer(
                                        activity,
                                        TestActivity.FRAGMENT_CONTAINER_VIEW,
                                        (preference, hostOfPreference) -> preference.isVisible()))),
                activity);
    }

    private static FragmentFactory createFragmentFactoryReturning(final Fragment fragment) {
        final DefaultFragmentFactory defaultFragmentFactory = new DefaultFragmentFactory();
        return new FragmentFactory() {

            @Override
            public <T extends Fragment> T instantiate(final Class<T> fragmentClassName,
                                                      final Optional<PreferenceWithHost> src,
                                                      final Context context,
                                                      final InstantiateAndInitializeFragment instantiateAndInitializeFragment) {
                return fragment.getClass().equals(fragmentClassName) ?
                        (T) fragment :
                        defaultFragmentFactory.instantiate(fragmentClassName, src, context, instantiateAndInitializeFragment);
            }
        };
    }
}
