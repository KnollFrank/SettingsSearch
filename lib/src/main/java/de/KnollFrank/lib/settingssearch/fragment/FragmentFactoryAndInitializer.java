package de.KnollFrank.lib.settingssearch.fragment;

import android.content.Context;

import androidx.fragment.app.Fragment;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceWithHost;

public class FragmentFactoryAndInitializer {

    private final FragmentFactory fragmentFactory;
    private final FragmentInitializer fragmentInitializer;

    public FragmentFactoryAndInitializer(final FragmentFactory fragmentFactory,
                                         final FragmentInitializer fragmentInitializer) {
        this.fragmentFactory = fragmentFactory;
        this.fragmentInitializer = fragmentInitializer;
    }

    public Fragment instantiateAndInitializeFragment(final String fragmentClassName,
                                                     final Optional<PreferenceWithHost> src,
                                                     final Context context) {
        final Fragment fragment = instantiateFragment(fragmentClassName, src, context);
        fragmentInitializer.initialize(fragment);
        return fragment;
    }

    public Fragment instantiateFragment(final String fragmentClassName,
                                        final Optional<PreferenceWithHost> src,
                                        final Context context) {
        return fragmentFactory.instantiate(fragmentClassName, src, context);
    }
}
