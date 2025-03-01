package de.KnollFrank.lib.settingssearch.fragment.navigation;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PrincipalAndProxyProvider;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;

class PrincipalProvider {

    private final PrincipalAndProxyProvider principalAndProxyProvider;
    private final InstantiateAndInitializeFragment instantiateAndInitializeFragment;

    public PrincipalProvider(final PrincipalAndProxyProvider principalAndProxyProvider,
                             final InstantiateAndInitializeFragment instantiateAndInitializeFragment) {
        this.principalAndProxyProvider = principalAndProxyProvider;
        this.instantiateAndInitializeFragment = instantiateAndInitializeFragment;
    }

    public Optional<Fragment> getPrincipal(final PreferenceFragmentCompat proxy) {
        return principalAndProxyProvider
                .getPrincipal(proxy.getClass())
                .map(principal -> instantiateAndInitializeFragment.instantiateAndInitializeFragment(principal, Optional.empty()));
    }
}
