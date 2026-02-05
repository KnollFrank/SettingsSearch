package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.FragmentClassOfActivity;
import de.KnollFrank.lib.settingssearch.FragmentOfActivity;
import de.KnollFrank.lib.settingssearch.PreferenceOfHostOfActivity;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactories;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;

public class PreferenceFragmentFactory<F extends Fragment, P extends PreferenceFragmentCompat & InitializePreferenceFragmentWithFragmentBeforeOnCreate<F>> {

    private final PrincipalAndProxy<F, P> principalAndProxy;

    public PreferenceFragmentFactory(final PrincipalAndProxy<F, P> principalAndProxy) {
        this.principalAndProxy = principalAndProxy;
    }

    public <T extends Fragment> Optional<FragmentOfActivity<T>> createPreferenceFragmentForFragmentClass(
            final FragmentClassOfActivity<T> fragmentClass,
            final Optional<PreferenceOfHostOfActivity> src,
            final Context context,
            final InstantiateAndInitializeFragment instantiateAndInitializeFragment) {
        final PreferenceFragmentFactoryWorker<F, P> preferenceFragmentFactoryWorker =
                new PreferenceFragmentFactoryWorker<>(
                        getPrincipal(fragmentClass),
                        getProxy(fragmentClass),
                        src,
                        context,
                        instantiateAndInitializeFragment);
        return preferenceFragmentFactoryWorker.createPreferenceFragmentForFragmentClass(fragmentClass.fragment());
    }

    private <T extends Fragment> FragmentClassOfActivity<F> getPrincipal(final FragmentClassOfActivity<T> fragmentClass) {
        return new FragmentClassOfActivity<>(
                principalAndProxy.principal(),
                fragmentClass.activityOfFragment());
    }

    private <T extends Fragment> FragmentClassOfActivity<P> getProxy(final FragmentClassOfActivity<T> fragmentClass) {
        return new FragmentClassOfActivity<>(
                principalAndProxy.proxy(),
                fragmentClass.activityOfFragment());
    }

    private static class PreferenceFragmentFactoryWorker<F extends Fragment, P extends PreferenceFragmentCompat & InitializePreferenceFragmentWithFragmentBeforeOnCreate<F>> {

        private final FragmentClassOfActivity<F> principal;
        private final FragmentClassOfActivity<P> proxy;
        private final Optional<PreferenceOfHostOfActivity> src;
        private final Context context;
        private final InstantiateAndInitializeFragment instantiateAndInitializeFragment;

        private PreferenceFragmentFactoryWorker(final FragmentClassOfActivity<F> principal,
                                                final FragmentClassOfActivity<P> proxy,
                                                final Optional<PreferenceOfHostOfActivity> src,
                                                final Context context,
                                                final InstantiateAndInitializeFragment instantiateAndInitializeFragment) {
            this.principal = principal;
            this.proxy = proxy;
            this.src = src;
            this.context = context;
            this.instantiateAndInitializeFragment = instantiateAndInitializeFragment;
        }

        public <T extends Fragment> Optional<FragmentOfActivity<T>> createPreferenceFragmentForFragmentClass(final Class<T> fragmentClass) {
            return canCreatePreferenceFragmentHavingClass(fragmentClass) ?
                    Optional.of((FragmentOfActivity<T>) instantiateProxyAndInitializeWithPrincipal()) :
                    Optional.empty();
        }

        private boolean canCreatePreferenceFragmentHavingClass(final Class<? extends Fragment> clazz) {
            return proxy.fragment().equals(clazz);
        }

        private FragmentOfActivity<P> instantiateProxyAndInitializeWithPrincipal() {
            final FragmentOfActivity<P> proxy = instantiateProxy();
            // FK-FIXME: an dieser Stelle wurde instantiateAndInitializeFragment() schon ausgeführt, also auch onCreate() weswegen das "BeforeOnCreate" in initializePreferenceFragmentWithFragmentBeforeOnCreate() eine falsche Aussage ist?
            proxy.fragment().initializePreferenceFragmentWithFragmentBeforeOnCreate(instantiatePrincipal().fragment());
            return proxy;
        }

        private FragmentOfActivity<P> instantiateProxy() {
            // FK-TODO: warum DefaultFragmentFactory? Ist das nicht zu speziell?
            return FragmentFactories
                    .createWrappedDefaultFragmentFactory()
                    .instantiate(proxy, src, context, instantiateAndInitializeFragment);
        }

        private FragmentOfActivity<F> instantiatePrincipal() {
            return instantiateAndInitializeFragment.instantiateAndInitializeFragment(principal, src);
        }
    }
}
