package de.KnollFrank.lib.settingssearch.fragment;

import android.content.Context;

import androidx.fragment.app.Fragment;

import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.FragmentClassOfActivity;
import de.KnollFrank.lib.settingssearch.FragmentOfActivity;
import de.KnollFrank.lib.settingssearch.PreferenceOfHostOfActivity;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.FragmentFactoryFactory;

public class FragmentFactoryTestFactory {

    public static FragmentFactory createFragmentFactoryReturning(final Fragment fragment) {
        return new FragmentFactory() {

            private final FragmentFactory delegate =
                    FragmentFactoryFactory.createFragmentFactory(
                            Set.of(),
                            FragmentFactories.createWrappedDefaultFragmentFactory());

            @Override
            public <T extends Fragment> FragmentOfActivity<T> instantiate(
                    final FragmentClassOfActivity<T> fragmentClass,
                    final Optional<PreferenceOfHostOfActivity> src,
                    final Context context,
                    final InstantiateAndInitializeFragment instantiateAndInitializeFragment) {
                return fragment.getClass().equals(fragmentClass.fragment()) ?
                        new FragmentOfActivity<>(
                                (T) fragment,
                                fragmentClass.activityOfFragment()) :
                        delegate.instantiate(fragmentClass, src, context, instantiateAndInitializeFragment);
            }
        };
    }
}
