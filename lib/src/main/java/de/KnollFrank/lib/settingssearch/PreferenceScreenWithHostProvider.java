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
            final Optional<PreferenceOfHostOfActivity> src) {
        return this
                .getPreferenceFragment(fragmentClass, src)
                .map(preferenceFragment ->
                             new PreferenceScreenOfHostOfActivity(
                                     preferenceFragment.getPreferenceScreen(),
                                     preferenceFragment,
                                     fragmentClass.activityOFragment()));
    }

    private Optional<? extends PreferenceFragmentCompat> getPreferenceFragment(
            final FragmentClassOfActivity<? extends Fragment> fragmentClass,
            final Optional<PreferenceOfHostOfActivity> src) {
        final Fragment fragment = instantiateAndInitializeFragment.instantiateAndInitializeFragment(fragmentClass, src);
        return fragment instanceof final PreferenceFragmentCompat preferenceFragment ?
                Optional.of(preferenceFragment) :
                instantiateProxyOfPrincipal(
                        new FragmentClassOfActivity<>(
                                fragment.getClass(),
                                fragmentClass.activityOFragment()));
    }

    private Optional<? extends PreferenceFragmentCompat> instantiateProxyOfPrincipal(final FragmentClassOfActivity<? extends Fragment> principal) {
        return this
                .getProxy(principal)
                .map(proxy -> instantiateAndInitializeFragment.instantiateAndInitializeFragment(proxy, Optional.empty()));
    }

    private Optional<? extends FragmentClassOfActivity<? extends PreferenceFragmentCompat>> getProxy(final FragmentClassOfActivity<? extends Fragment> principal) {
        return principalAndProxyProvider
                .getProxy(principal.fragment())
                .map(proxy ->
                             new FragmentClassOfActivity<>(
                                     proxy,
                                     principal.activityOFragment()));
    }
}