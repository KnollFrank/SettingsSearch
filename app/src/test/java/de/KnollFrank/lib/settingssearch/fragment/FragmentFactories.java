package de.KnollFrank.lib.settingssearch.fragment;

import android.content.Context;

import androidx.fragment.app.Fragment;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.FragmentClassOfActivity;
import de.KnollFrank.lib.settingssearch.FragmentOfActivity;
import de.KnollFrank.lib.settingssearch.PreferenceOfHostOfActivity;

public class FragmentFactories {

    public static FragmentFactory createFragmentFactoryReturning(final Fragment fragment) {
        return new FragmentFactory() {

            private final FragmentFactory delegate = new DefaultFragmentFactory();

            @Override
            public <T extends Fragment> FragmentOfActivity<T> instantiate(
                    final FragmentClassOfActivity<T> fragmentClass,
                    final Optional<PreferenceOfHostOfActivity> src,
                    final Context context,
                    final InstantiateAndInitializeFragment instantiateAndInitializeFragment) {
                return fragment.getClass().equals(fragmentClass.fragment()) ?
                        new FragmentOfActivity<>(
                                (T) fragment,
                                fragmentClass.activityOFragment()) :
                        delegate.instantiate(fragmentClass, src, context, instantiateAndInitializeFragment);
            }
        };
    }
}
