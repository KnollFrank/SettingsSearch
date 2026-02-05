package de.KnollFrank.lib.settingssearch.fragment;

import android.content.Context;

import androidx.fragment.app.Fragment;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.FragmentClassOfActivity;
import de.KnollFrank.lib.settingssearch.FragmentOfActivity;
import de.KnollFrank.lib.settingssearch.PreferenceOfHostOfActivity;

public class FragmentFactories {

    private FragmentFactories() {
    }

    public static FragmentFactory wrap(final de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.FragmentFactory fragmentFactory) {
        return new FragmentFactory() {

            @Override
            public <T extends Fragment> FragmentOfActivity<T> instantiate(final FragmentClassOfActivity<T> fragmentClass,
                                                                          final Optional<PreferenceOfHostOfActivity> src,
                                                                          final Context context,
                                                                          final InstantiateAndInitializeFragment instantiateAndInitializeFragment) {
                return new FragmentOfActivity<>(
                        fragmentFactory.instantiate(fragmentClass, src, context, instantiateAndInitializeFragment),
                        fragmentClass.activityOfFragment());
            }
        };
    }

    public static FragmentFactory createWrappedDefaultFragmentFactory() {
        return wrap(new DefaultFragmentFactory());
    }
}
