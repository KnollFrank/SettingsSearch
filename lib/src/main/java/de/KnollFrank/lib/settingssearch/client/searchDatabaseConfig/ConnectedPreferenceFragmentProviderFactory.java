package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.ConnectedPreferenceFragmentProvider;
import de.KnollFrank.lib.settingssearch.common.Maps;

// FK-TODO: rename to ProxyProviderFactory
public class ConnectedPreferenceFragmentProviderFactory {

    // FK-TODO: rename to createProxyProviderFactory()
    public static ConnectedPreferenceFragmentProvider createConnectedPreferenceFragmentProvider(final Set<FragmentAndProxy<? extends Fragment, ? extends PreferenceFragmentCompat>> fragmentAndProxies) {
        final var preferenceFragmentByFragment = getPreferenceFragmentByFragmentBiMap(fragmentAndProxies);
        return fragment -> Maps.get(preferenceFragmentByFragment, fragment);
    }

    // FK-TODO: rename to getProxyByFragmentBiMap()
    static BiMap<Class<? extends Fragment>, Class<? extends PreferenceFragmentCompat>> getPreferenceFragmentByFragmentBiMap(final Set<FragmentAndProxy<? extends Fragment, ? extends PreferenceFragmentCompat>> fragmentAndProxies) {
        return ImmutableBiMap
                .<Class<? extends Fragment>, Class<? extends PreferenceFragmentCompat>>builder()
                .putAll(getPreferenceFragmentByFragmentMap(fragmentAndProxies))
                .build();
    }

    // FK-TODO: rename to getProxyByFragmentMap()
    private static Map<Class<? extends Fragment>, Class<? extends PreferenceFragmentCompat>> getPreferenceFragmentByFragmentMap(final Set<FragmentAndProxy<? extends Fragment, ? extends PreferenceFragmentCompat>> fragmentAndProxies) {
        return fragmentAndProxies
                .stream()
                .collect(
                        Collectors.toMap(
                                FragmentAndProxy::fragment,
                                FragmentAndProxy::proxy));
    }
}
