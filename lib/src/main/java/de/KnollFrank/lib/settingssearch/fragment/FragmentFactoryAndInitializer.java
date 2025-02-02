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

    public Fragment instantiateAndInitializeFragment(final Class<? extends Fragment> fragmentClass,
                                                     final Optional<PreferenceWithHost> src,
                                                     final Context context,
                                                     final Fragments fragments) {
        final Fragment fragment = instantiateFragment(fragmentClass, src, context, fragments);
        fragmentInitializer.initialize(fragment);
        return fragment;
    }

    private Fragment instantiateFragment(final Class<? extends Fragment> fragmentClass,
                                         final Optional<PreferenceWithHost> src,
                                         final Context context,
                                         final Fragments fragments) {
        return fragmentFactory.instantiate(fragmentClass, src, context, fragments);
    }
}
