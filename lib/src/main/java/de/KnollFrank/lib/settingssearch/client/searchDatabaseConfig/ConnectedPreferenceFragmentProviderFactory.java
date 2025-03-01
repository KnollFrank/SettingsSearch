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

public class ConnectedPreferenceFragmentProviderFactory {

    public static ConnectedPreferenceFragmentProvider createConnectedPreferenceFragmentProvider(final Set<FragmentWithPreferenceFragmentConnection<? extends Fragment, ? extends PreferenceFragmentCompat>> fragmentWithPreferenceFragmentConnections) {
        final var preferenceFragmentByFragment = getPreferenceFragmentByFragmentBiMap(fragmentWithPreferenceFragmentConnections);
        return fragment -> Maps.get(preferenceFragmentByFragment, fragment);
    }

    static BiMap<Class<? extends Fragment>, Class<? extends PreferenceFragmentCompat>> getPreferenceFragmentByFragmentBiMap(final Set<FragmentWithPreferenceFragmentConnection<? extends Fragment, ? extends PreferenceFragmentCompat>> fragmentWithPreferenceFragmentConnections) {
        return ImmutableBiMap
                .<Class<? extends Fragment>, Class<? extends PreferenceFragmentCompat>>builder()
                .putAll(getPreferenceFragmentByFragmentMap(fragmentWithPreferenceFragmentConnections))
                .build();
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
