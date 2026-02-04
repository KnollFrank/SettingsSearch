package de.KnollFrank.lib.settingssearch.fragment.navigation;

import androidx.fragment.app.Fragment;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.FragmentClassOfActivity;
import de.KnollFrank.lib.settingssearch.PreferenceFragmentOfActivity;
import de.KnollFrank.lib.settingssearch.PreferenceOfHostOfActivity;
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

    public Optional<Fragment> getPrincipal(final PreferenceFragmentOfActivity proxy, final Optional<PreferenceOfHostOfActivity> src) {
        return principalAndProxyProvider
                .getPrincipal(proxy.preferenceFragment().getClass())
                .map(principal ->
                             new FragmentClassOfActivity<>(
                                     principal,
                                     proxy.activityOfPreferenceFragment()))
                .map(principal -> instantiateAndInitializeFragment.instantiateAndInitializeFragment(principal, src));
    }
}
