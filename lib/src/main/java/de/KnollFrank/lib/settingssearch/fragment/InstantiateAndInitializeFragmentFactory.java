package de.KnollFrank.lib.settingssearch.fragment;

import android.content.Context;

import de.KnollFrank.lib.settingssearch.fragment.factory.FragmentFactoryAndInitializerRegistry;

public class InstantiateAndInitializeFragmentFactory {

    private InstantiateAndInitializeFragmentFactory() {
    }

    public static InstantiateAndInitializeFragment createInstantiateAndInitializeFragment(
            final FragmentFactory fragmentFactory,
            final FragmentInitializer fragmentInitializer,
            final Context context) {
        return new Fragments(
                new FragmentFactoryAndInitializerRegistry(
                        new FragmentFactoryAndInitializer(
                                fragmentFactory,
                                fragmentInitializer)),
                context);
    }
}
