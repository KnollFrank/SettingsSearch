package de.KnollFrank.lib.settingssearch.fragment.navigation;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.FragmentClassOfActivity;
import de.KnollFrank.lib.settingssearch.FragmentOfActivity;
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

    public Optional<FragmentOfActivity<? extends Fragment>> getPrincipal(
            final FragmentOfActivity<? extends PreferenceFragmentCompat> proxy,
            final Optional<PreferenceOfHostOfActivity> src) {
        return principalAndProxyProvider
                .getPrincipal(proxy.fragment().getClass())
                .map(principal ->
                             new FragmentClassOfActivity<>(
                                     principal,
                                     proxy.activityOfFragment()))
                .map(principal -> instantiateAndInitializeFragment.instantiateAndInitializeFragment(principal, src));
    }
}
