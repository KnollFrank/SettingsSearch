package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.ConnectedPreferenceFragmentProvider;
import de.KnollFrank.lib.settingssearch.common.Maps;

class ConnectedPreferenceFragmentProviderFactory {

    public static ConnectedPreferenceFragmentProvider createConnectedPreferenceFragmentProvider(final Set<FragmentWithPreferenceFragmentConnection<? extends Fragment, ? extends PreferenceFragmentCompat>> fragmentWithPreferenceFragmentConnections) {
        final var preferenceFragmentByFragment = getPreferenceFragmentByFragmentMap(fragmentWithPreferenceFragmentConnections);
        return fragment -> Maps.get(preferenceFragmentByFragment, fragment);
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
