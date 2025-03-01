package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.ConnectedFragmentProvider;
import de.KnollFrank.lib.settingssearch.common.Maps;

class ConnectedFragmentProviderFactory {

    public static ConnectedFragmentProvider createConnectedFragmentProvider(final Set<FragmentWithPreferenceFragmentConnection<? extends Fragment, ? extends PreferenceFragmentCompat>> fragmentWithPreferenceFragmentConnections) {
        final var fragmentByPreferenceFragment = getFragmentByPreferenceFragmentMap(fragmentWithPreferenceFragmentConnections);
        return preferenceFragment -> Maps.get(fragmentByPreferenceFragment, preferenceFragment);
    }

    private static Map<Class<? extends PreferenceFragmentCompat>, Class<? extends Fragment>> getFragmentByPreferenceFragmentMap(final Set<FragmentWithPreferenceFragmentConnection<? extends Fragment, ? extends PreferenceFragmentCompat>> fragmentWithPreferenceFragmentConnections) {
        return fragmentWithPreferenceFragmentConnections
                .stream()
                .collect(
                        Collectors.toMap(
                                FragmentWithPreferenceFragmentConnection::preferenceFragment,
                                FragmentWithPreferenceFragmentConnection::fragment));
    }
}
