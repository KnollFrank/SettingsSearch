package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Set;

import de.KnollFrank.lib.settingssearch.PrincipalProvider;
import de.KnollFrank.lib.settingssearch.common.Maps;

class PrincipalProviderFactory {

    public static PrincipalProvider createPrincipalProvider(final Set<PrincipalAndProxy<? extends Fragment, ? extends PreferenceFragmentCompat>> principalAndProxies) {
        final var fragmentByPreferenceFragment =
                ProxyProviderFactory
                        .getProxyByPrincipalBiMap(principalAndProxies)
                        .inverse();
        return proxy -> Maps.get(fragmentByPreferenceFragment, proxy);
    }
}
