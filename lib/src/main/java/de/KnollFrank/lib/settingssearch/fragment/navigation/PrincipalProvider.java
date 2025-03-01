package de.KnollFrank.lib.settingssearch.fragment.navigation;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;

class PrincipalProvider {

    private final de.KnollFrank.lib.settingssearch.PrincipalProvider principalProvider;
    private final InstantiateAndInitializeFragment instantiateAndInitializeFragment;

    public PrincipalProvider(final de.KnollFrank.lib.settingssearch.PrincipalProvider principalProvider,
                             final InstantiateAndInitializeFragment instantiateAndInitializeFragment) {
        this.principalProvider = principalProvider;
        this.instantiateAndInitializeFragment = instantiateAndInitializeFragment;
    }

    public Optional<Fragment> getPrincipal(final PreferenceFragmentCompat proxy) {
        return principalProvider
                .getPrincipal(proxy.getClass())
                .map(fragment -> instantiateAndInitializeFragment.instantiateAndInitializeFragment(fragment, Optional.empty()));
    }
}
