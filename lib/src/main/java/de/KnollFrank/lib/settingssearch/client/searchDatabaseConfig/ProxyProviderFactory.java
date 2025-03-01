package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.ProxyProvider;
import de.KnollFrank.lib.settingssearch.common.Maps;

public class ProxyProviderFactory {

    public static ProxyProvider createProxyProviderFactory(final Set<PrincipalAndProxy<? extends Fragment, ? extends PreferenceFragmentCompat>> principalAndProxies) {
        final var proxyByPrincipal = getProxyByPrincipalBiMap(principalAndProxies);
        return principal -> Maps.get(proxyByPrincipal, principal);
    }

    static BiMap<Class<? extends Fragment>, Class<? extends PreferenceFragmentCompat>> getProxyByPrincipalBiMap(final Set<PrincipalAndProxy<? extends Fragment, ? extends PreferenceFragmentCompat>> principalAndProxies) {
        return ImmutableBiMap
                .<Class<? extends Fragment>, Class<? extends PreferenceFragmentCompat>>builder()
                .putAll(getProxyByPrincipalMap(principalAndProxies))
                .build();
    }

    private static Map<Class<? extends Fragment>, Class<? extends PreferenceFragmentCompat>> getProxyByPrincipalMap(final Set<PrincipalAndProxy<? extends Fragment, ? extends PreferenceFragmentCompat>> principalAndProxies) {
        return principalAndProxies
                .stream()
                .collect(
                        Collectors.toMap(
                                PrincipalAndProxy::principal,
                                PrincipalAndProxy::proxy));
    }
}
