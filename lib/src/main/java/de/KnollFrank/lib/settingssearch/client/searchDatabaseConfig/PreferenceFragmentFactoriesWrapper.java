package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.FragmentClassOfActivity;
import de.KnollFrank.lib.settingssearch.FragmentOfActivity;
import de.KnollFrank.lib.settingssearch.PreferenceOfHostOfActivity;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;

class PreferenceFragmentFactoriesWrapper implements FragmentFactory {

    private final Set<PreferenceFragmentFactory<? extends Fragment, ? extends PreferenceFragmentCompat>> preferenceFragmentFactories;
    private final FragmentFactory delegate;

    public PreferenceFragmentFactoriesWrapper(final Set<PreferenceFragmentFactory<? extends Fragment, ? extends PreferenceFragmentCompat>> preferenceFragmentFactories,
                                              final FragmentFactory delegate) {
        this.preferenceFragmentFactories = preferenceFragmentFactories;
        this.delegate = delegate;
    }

    @Override
    public <T extends Fragment> FragmentOfActivity<T> instantiate(
            final FragmentClassOfActivity<T> fragmentClass,
            final Optional<PreferenceOfHostOfActivity> src,
            final Context context,
            final InstantiateAndInitializeFragment instantiateAndInitializeFragment) {
        return this
                .createPreferenceFragment(fragmentClass, src, context, instantiateAndInitializeFragment)
                .orElseGet(() -> delegate.instantiate(fragmentClass, src, context, instantiateAndInitializeFragment));
    }

    private <T extends Fragment> Optional<FragmentOfActivity<T>> createPreferenceFragment(
            final FragmentClassOfActivity<T> fragmentClass,
            final Optional<PreferenceOfHostOfActivity> src,
            final Context context,
            final InstantiateAndInitializeFragment instantiateAndInitializeFragment) {
        return preferenceFragmentFactories
                .stream()
                .map(preferenceFragmentFactory -> preferenceFragmentFactory.createPreferenceFragmentForFragmentClass(fragmentClass, src, context, instantiateAndInitializeFragment))
                .flatMap(Optional::stream)
                .findFirst();
    }
}
