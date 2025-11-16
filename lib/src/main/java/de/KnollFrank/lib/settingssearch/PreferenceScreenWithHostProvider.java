package de.KnollFrank.lib.settingssearch;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;

public class PreferenceScreenWithHostProvider {

    private final InstantiateAndInitializeFragment instantiateAndInitializeFragment;
    private final PrincipalAndProxyProvider principalAndProxyProvider;

    public PreferenceScreenWithHostProvider(final InstantiateAndInitializeFragment instantiateAndInitializeFragment,
                                            final PrincipalAndProxyProvider principalAndProxyProvider) {
        this.instantiateAndInitializeFragment = instantiateAndInitializeFragment;
        this.principalAndProxyProvider = principalAndProxyProvider;
    }

    public Optional<PreferenceScreenWithHost> getPreferenceScreenWithHostOfFragment(
            final Class<? extends Fragment> fragmentClass,
            final Optional<PreferenceWithHost> src) {
        return this
                .getPreferenceFragment(fragmentClass, src)
                .map(this::getPreferenceScreenWithHost);
    }

    private Optional<? extends PreferenceFragmentCompat> getPreferenceFragment(
            final Class<? extends Fragment> fragmentClass,
            final Optional<PreferenceWithHost> src) {
        final Fragment fragment = instantiateAndInitializeFragment.instantiateAndInitializeFragment(fragmentClass, src);
        return fragment instanceof final PreferenceFragmentCompat preferenceFragment ?
                Optional.of(preferenceFragment) :
                principalAndProxyProvider
                        .getProxy(fragment.getClass())
                        .map(preferenceFragment -> instantiateAndInitializeFragment.instantiateAndInitializeFragment(preferenceFragment, Optional.empty()));
    }

    private PreferenceScreenWithHost getPreferenceScreenWithHost(final PreferenceFragmentCompat preferenceFragment) {
        return new PreferenceScreenWithHost(
                preferenceFragment.getPreferenceScreen(),
                preferenceFragment);
    }
}