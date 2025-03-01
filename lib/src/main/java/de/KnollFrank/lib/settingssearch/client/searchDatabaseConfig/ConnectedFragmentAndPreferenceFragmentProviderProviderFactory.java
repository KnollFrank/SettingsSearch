package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.ConnectedFragmentProvider;
import de.KnollFrank.lib.settingssearch.ConnectedPreferenceFragmentProvider;
import de.KnollFrank.lib.settingssearch.common.Maps;

class ConnectedFragmentAndPreferenceFragmentProviderProviderFactory {

    public static ConnectedFragmentProvider createConnectedFragmentProvider(final Set<FragmentWithPreferenceFragmentConnection<? extends Fragment, ? extends PreferenceFragmentCompat>> fragmentWithPreferenceFragmentConnections) {
        final var fragmentByPreferenceFragment = getPreferenceFragmentByFragmentBiMap(fragmentWithPreferenceFragmentConnections).inverse();
        return preferenceFragment -> Maps.get(fragmentByPreferenceFragment, preferenceFragment);
    }

    public static ConnectedPreferenceFragmentProvider createConnectedPreferenceFragmentProvider(final Set<FragmentWithPreferenceFragmentConnection<? extends Fragment, ? extends PreferenceFragmentCompat>> fragmentWithPreferenceFragmentConnections) {
        final var preferenceFragmentByFragment = getPreferenceFragmentByFragmentBiMap(fragmentWithPreferenceFragmentConnections);
        return fragment -> Maps.get(preferenceFragmentByFragment, fragment);
    }

    private static BiMap<Class<? extends Fragment>, Class<? extends PreferenceFragmentCompat>> getPreferenceFragmentByFragmentBiMap(final Set<FragmentWithPreferenceFragmentConnection<? extends Fragment, ? extends PreferenceFragmentCompat>> fragmentWithPreferenceFragmentConnections) {
        return HashBiMap.create(getPreferenceFragmentByFragmentMap(fragmentWithPreferenceFragmentConnections));
    }

    private static Map<Class<? extends Fragment>, Class<? extends PreferenceFragmentCompat>> getPreferenceFragmentByFragmentMap(final Set<FragmentWithPreferenceFragmentConnection<? extends Fragment, ? extends PreferenceFragmentCompat>> fragmentWithPreferenceFragmentConnections) {
        return fragmentWithPreferenceFragmentConnections
                .stream()
                .collect(
                        Collectors.toMap(
                                FragmentWithPreferenceFragmentConnection::fragment,
                                FragmentWithPreferenceFragmentConnection::preferenceFragment));
    }
}
