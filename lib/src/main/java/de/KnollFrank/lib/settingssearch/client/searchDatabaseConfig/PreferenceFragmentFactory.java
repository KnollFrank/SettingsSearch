package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceWithHost;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;

public class PreferenceFragmentFactory<F extends Fragment, P extends PreferenceFragmentCompat & InitializePreferenceFragmentWithFragmentBeforeOnCreate<F>> {

    private final FragmentWithPreferenceFragmentConnection<F, P> fragmentWithPreferenceFragmentConnection;

    public PreferenceFragmentFactory(final FragmentWithPreferenceFragmentConnection<F, P> fragmentWithPreferenceFragmentConnection) {
        this.fragmentWithPreferenceFragmentConnection = fragmentWithPreferenceFragmentConnection;
    }

    public <T extends Fragment> Optional<T> createPreferenceFragmentForClass(
            final Class<T> clazz,
            final Optional<PreferenceWithHost> src,
            final Context context,
            final InstantiateAndInitializeFragment instantiateAndInitializeFragment) {
        return canCreatePreferenceFragmentHavingClass(clazz) ?
                Optional.of((T) createPreferenceFragmentAndInitializeWithFragment(src, context, instantiateAndInitializeFragment)) :
                Optional.empty();
    }

    private boolean canCreatePreferenceFragmentHavingClass(final Class<? extends Fragment> clazz) {
        return fragmentWithPreferenceFragmentConnection.preferenceFragment().equals(clazz);
    }

    private P createPreferenceFragmentAndInitializeWithFragment(
            final Optional<PreferenceWithHost> src,
            final Context context,
            final InstantiateAndInitializeFragment instantiateAndInitializeFragment) {
        final P preferenceFragment = createPreferenceFragment(src, context, instantiateAndInitializeFragment);
        preferenceFragment.initializePreferenceFragmentWithFragmentBeforeOnCreate(getFragment(instantiateAndInitializeFragment));
        return preferenceFragment;
    }

    private P createPreferenceFragment(final Optional<PreferenceWithHost> src,
                                       final Context context,
                                       final InstantiateAndInitializeFragment instantiateAndInitializeFragment) {
        return new DefaultFragmentFactory().instantiate(
                fragmentWithPreferenceFragmentConnection.preferenceFragment(),
                src,
                context,
                instantiateAndInitializeFragment);
    }

    private F getFragment(final InstantiateAndInitializeFragment instantiateAndInitializeFragment) {
        return instantiateAndInitializeFragment.instantiateAndInitializeFragment(
                fragmentWithPreferenceFragmentConnection.fragment(),
                Optional.empty());
    }
}
