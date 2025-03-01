package de.KnollFrank.lib.settingssearch;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import com.google.common.collect.BiMap;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.common.Maps;

public class PrincipalAndProxyProvider {

    private final BiMap<Class<? extends Fragment>, Class<? extends PreferenceFragmentCompat>> proxyByPrincipal;

    public PrincipalAndProxyProvider(final BiMap<Class<? extends Fragment>, Class<? extends PreferenceFragmentCompat>> proxyByPrincipal) {
        this.proxyByPrincipal = proxyByPrincipal;
    }

    public Optional<Class<? extends Fragment>> getPrincipal(Class<? extends PreferenceFragmentCompat> proxy) {
        return Maps.get(principalByProxy(), proxy);
    }

    public Optional<Class<? extends PreferenceFragmentCompat>> getProxy(Class<? extends Fragment> principal) {
        return Maps.get(proxyByPrincipal, principal);
    }

    private BiMap<Class<? extends PreferenceFragmentCompat>, Class<? extends Fragment>> principalByProxy() {
        return proxyByPrincipal.inverse();
    }
}
