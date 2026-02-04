package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.FragmentClassOfActivity;
import de.KnollFrank.lib.settingssearch.PreferenceOfHostOfActivity;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;

class FragmentFactory implements de.KnollFrank.lib.settingssearch.fragment.FragmentFactory {

    private final Set<PreferenceFragmentFactory<? extends Fragment, ? extends PreferenceFragmentCompat>> preferenceFragmentFactories;
    private final de.KnollFrank.lib.settingssearch.fragment.FragmentFactory delegate;

    public FragmentFactory(final Set<PreferenceFragmentFactory<? extends Fragment, ? extends PreferenceFragmentCompat>> preferenceFragmentFactories,
                           final de.KnollFrank.lib.settingssearch.fragment.FragmentFactory delegate) {
        this.preferenceFragmentFactories = preferenceFragmentFactories;
        this.delegate = delegate;
    }

    @Override
    public <T extends Fragment> T instantiate(final FragmentClassOfActivity<T> fragmentClass,
                                              final Optional<PreferenceOfHostOfActivity> src,
                                              final Context context,
                                              final InstantiateAndInitializeFragment instantiateAndInitializeFragment) {
        return this
                .createPreferenceFragment(fragmentClass, src, context, instantiateAndInitializeFragment)
                .orElseGet(() -> delegate.instantiate(fragmentClass, src, context, instantiateAndInitializeFragment));
    }

    private <T extends Fragment> Optional<T> createPreferenceFragment(
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
