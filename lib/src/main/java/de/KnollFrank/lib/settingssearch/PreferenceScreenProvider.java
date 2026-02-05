package de.KnollFrank.lib.settingssearch;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;

public class PreferenceScreenProvider {

    private final InstantiateAndInitializeFragment instantiateAndInitializeFragment;
    private final PrincipalAndProxyProvider principalAndProxyProvider;

    public PreferenceScreenProvider(final InstantiateAndInitializeFragment instantiateAndInitializeFragment,
                                    final PrincipalAndProxyProvider principalAndProxyProvider) {
        this.instantiateAndInitializeFragment = instantiateAndInitializeFragment;
        this.principalAndProxyProvider = principalAndProxyProvider;
    }

    public Optional<PreferenceScreenOfHostOfActivity> getPreferenceScreen(
            final FragmentClassOfActivity<? extends Fragment> fragmentClass,
            final Optional<PreferenceOfHostOfActivity> src) {
        return this
                .getPreferenceFragment(fragmentClass, src)
                .map(preferenceFragment ->
                             new PreferenceScreenOfHostOfActivity(
                                     preferenceFragment.fragment().getPreferenceScreen(),
                                     preferenceFragment.fragment(),
                                     fragmentClass.activityOFragment()));
    }

    private Optional<FragmentOfActivity<? extends PreferenceFragmentCompat>> getPreferenceFragment(
            final FragmentClassOfActivity<? extends Fragment> fragmentClass,
            final Optional<PreferenceOfHostOfActivity> src) {
        final FragmentOfActivity<? extends Fragment> fragment = instantiateAndInitializeFragment.instantiateAndInitializeFragment(fragmentClass, src);
        return fragment.fragment() instanceof final PreferenceFragmentCompat preferenceFragment ?
                Optional.of(
                        new FragmentOfActivity<>(
                                preferenceFragment,
                                fragment.activityOfFragment())) :
                instantiateProxyOfPrincipal(fragmentClass);
    }

    private Optional<FragmentOfActivity<? extends PreferenceFragmentCompat>> instantiateProxyOfPrincipal(final FragmentClassOfActivity<? extends Fragment> principal) {
        return this
                .getProxy(principal)
                .map(proxy -> instantiateAndInitializeFragment.instantiateAndInitializeFragment(proxy, Optional.empty()));
    }

    private Optional<FragmentClassOfActivity<? extends PreferenceFragmentCompat>> getProxy(final FragmentClassOfActivity<? extends Fragment> principal) {
        return principalAndProxyProvider
                .getProxy(principal.fragment())
                .map(proxy ->
                             new FragmentClassOfActivity<>(
                                     proxy,
                                     principal.activityOFragment()));
    }
}