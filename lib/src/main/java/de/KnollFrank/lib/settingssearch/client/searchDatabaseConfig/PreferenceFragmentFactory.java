package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceOfHost;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;

public class PreferenceFragmentFactory<F extends Fragment, P extends PreferenceFragmentCompat & InitializePreferenceFragmentWithFragmentBeforeOnCreate<F>> {

    private final PrincipalAndProxy<F, P> principalAndProxy;

    public PreferenceFragmentFactory(final PrincipalAndProxy<F, P> principalAndProxy) {
        this.principalAndProxy = principalAndProxy;
    }

    public <T extends Fragment> Optional<T> createPreferenceFragmentForClass(
            final Class<T> clazz,
            final Optional<PreferenceOfHost> src,
            final Context context,
            final InstantiateAndInitializeFragment instantiateAndInitializeFragment) {
        return canCreatePreferenceFragmentHavingClass(clazz) ?
                Optional.of((T) createPreferenceFragmentAndInitializeWithFragment(src, context, instantiateAndInitializeFragment)) :
                Optional.empty();
    }

    private boolean canCreatePreferenceFragmentHavingClass(final Class<? extends Fragment> clazz) {
        return principalAndProxy.proxy().equals(clazz);
    }

    private P createPreferenceFragmentAndInitializeWithFragment(
            final Optional<PreferenceOfHost> src,
            final Context context,
            final InstantiateAndInitializeFragment instantiateAndInitializeFragment) {
        final P preferenceFragment = createPreferenceFragment(src, context, instantiateAndInitializeFragment);
        // FK-FIXME: an dieser Stelle wurde instantiateAndInitializeFragment() schon ausgeführt, also auch onCreate() weswegen das "BeforeOnCreate" in initializePreferenceFragmentWithFragmentBeforeOnCreate() eine falsche Aussage ist?
        preferenceFragment.initializePreferenceFragmentWithFragmentBeforeOnCreate(getFragment(instantiateAndInitializeFragment, src));
        return preferenceFragment;
    }

    private P createPreferenceFragment(final Optional<PreferenceOfHost> src,
                                       final Context context,
                                       final InstantiateAndInitializeFragment instantiateAndInitializeFragment) {
        // FK-TODO: warum DefaultFragmentFactory? Ist das nicht zu speziell?
        return new DefaultFragmentFactory().instantiate(
                principalAndProxy.proxy(),
                src,
                context,
                instantiateAndInitializeFragment);
    }

    private F getFragment(final InstantiateAndInitializeFragment instantiateAndInitializeFragment,
                          final Optional<PreferenceOfHost> src) {
        return instantiateAndInitializeFragment.instantiateAndInitializeFragment(
                principalAndProxy.principal(),
                src);
    }
}
