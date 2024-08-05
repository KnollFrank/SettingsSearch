package de.KnollFrank.lib.preferencesearch.fragment;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;

import java.util.Optional;

public class DefaultFragmentFactoryAndInitializer implements FragmentFactoryAndInitializer {

    private final FragmentFactory fragmentFactory;
    private final FragmentInitializer fragmentInitializer;

    public DefaultFragmentFactoryAndInitializer(final FragmentFactory fragmentFactory, final FragmentInitializer fragmentInitializer) {
        this.fragmentFactory = fragmentFactory;
        this.fragmentInitializer = fragmentInitializer;
    }

    @Override
    public Fragment instantiateAndInitializeFragment(final String fragmentClassName,
                                                     final Optional<Preference> src,
                                                     final Context context) {
        final Fragment fragment = fragmentFactory.instantiate(fragmentClassName, src, context);
        fragmentInitializer.initialize(fragment);
        return fragment;
    }
}
