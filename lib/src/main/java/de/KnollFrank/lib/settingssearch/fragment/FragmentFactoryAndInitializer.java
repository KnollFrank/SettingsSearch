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

    public <T extends Fragment> T instantiateAndInitializeFragment(final Class<T> fragmentClass,
                                                                   final Optional<PreferenceWithHost> src,
                                                                   final Context context,
                                                                   final IFragments fragments) {
        final T fragment = instantiateFragment(fragmentClass, src, context, fragments);
        fragmentInitializer.initialize(fragment);
        return fragment;
    }

    private <T extends Fragment> T instantiateFragment(final Class<T> fragmentClass,
                                                       final Optional<PreferenceWithHost> src,
                                                       final Context context,
                                                       final IFragments fragments) {
        return fragmentFactory.instantiate(fragmentClass, src, context, fragments);
    }
}
