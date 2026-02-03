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

    public Optional<PreferenceScreenOfHostOfActivity> getPreferenceScreenWithHostOfFragment(
            final FragmentClassOfActivity<? extends Fragment> fragmentClass,
            final Optional<PreferenceOfHost> src) {
        return this
                .getPreferenceFragment(fragmentClass.fragment(), src)
                .map(preferenceFragment ->
                             new PreferenceScreenOfHostOfActivity(
                                     preferenceFragment.getPreferenceScreen(),
                                     preferenceFragment,
                                     fragmentClass.activityOFragment()));
    }

    private Optional<? extends PreferenceFragmentCompat> getPreferenceFragment(
            final Class<? extends Fragment> fragmentClass,
            final Optional<PreferenceOfHost> src) {
        final Fragment fragment = instantiateAndInitializeFragment.instantiateAndInitializeFragment(fragmentClass, src);
        return fragment instanceof final PreferenceFragmentCompat preferenceFragment ?
                Optional.of(preferenceFragment) :
                principalAndProxyProvider
                        .getProxy(fragment.getClass())
                        .map(preferenceFragment -> instantiateAndInitializeFragment.instantiateAndInitializeFragment(preferenceFragment, Optional.empty()));
    }
}