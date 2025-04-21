package de.KnollFrank.lib.settingssearch.fragment;

import android.content.Context;

import de.KnollFrank.lib.settingssearch.fragment.factory.FragmentFactoryAndInitializerWithCache;

public class InstantiateAndInitializeFragmentFactory {

    public static InstantiateAndInitializeFragment createInstantiateAndInitializeFragment(
            final FragmentFactory fragmentFactory,
            final FragmentInitializer fragmentInitializer,
            final Context context) {
        return new Fragments(
                new FragmentFactoryAndInitializerWithCache(
                        new FragmentFactoryAndInitializer(
                                fragmentFactory,
                                fragmentInitializer)),
                context);
    }
}
