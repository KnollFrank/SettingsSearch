package de.KnollFrank.lib.settingssearch.fragment;

import android.content.Context;

import androidx.fragment.app.Fragment;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.FragmentClassOfActivity;
import de.KnollFrank.lib.settingssearch.FragmentOfActivity;
import de.KnollFrank.lib.settingssearch.PreferenceOfHostOfActivity;

public class FragmentFactoryAndInitializer {

    private final FragmentFactory fragmentFactory;
    private final FragmentInitializer fragmentInitializer;

    public FragmentFactoryAndInitializer(final FragmentFactory fragmentFactory,
                                         final FragmentInitializer fragmentInitializer) {
        this.fragmentFactory = fragmentFactory;
        this.fragmentInitializer = fragmentInitializer;
    }

    public <T extends Fragment> FragmentOfActivity<T> instantiateAndInitializeFragment(
            final FragmentClassOfActivity<T> fragmentClass,
            final Optional<PreferenceOfHostOfActivity> src,
            final Context context,
            // FK-TODO: make InstantiateAndInitializeFragment an instance variable?
            final InstantiateAndInitializeFragment instantiateAndInitializeFragment) {
        final FragmentOfActivity<T> fragment = fragmentFactory.instantiate(fragmentClass, src, context, instantiateAndInitializeFragment);
        fragmentInitializer.initialize(fragment.fragment());
        return fragment;
    }
}
