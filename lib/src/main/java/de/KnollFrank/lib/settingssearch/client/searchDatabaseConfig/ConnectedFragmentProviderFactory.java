package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Map;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.ConnectedFragmentProvider;
import de.KnollFrank.lib.settingssearch.common.Maps;

class ConnectedFragmentProviderFactory {

    public static ConnectedFragmentProvider createConnectedFragmentProvider(final ActivitySearchDatabaseConfigs activitySearchDatabaseConfigs) {
        final var fragmentByPreferenceFragment = getFragmentByPreferenceFragmentMap(activitySearchDatabaseConfigs);
        return preferenceFragment -> Maps.get(fragmentByPreferenceFragment, preferenceFragment);
    }

    private static Map<Class<? extends PreferenceFragmentCompat>, Class<? extends Fragment>> getFragmentByPreferenceFragmentMap(final ActivitySearchDatabaseConfigs activitySearchDatabaseConfigs) {
        return activitySearchDatabaseConfigs
                .fragmentWithPreferenceFragmentConnections()
                .stream()
                .collect(
                        Collectors.toMap(
                                FragmentWithPreferenceFragmentConnection::preferenceFragment,
                                FragmentWithPreferenceFragmentConnection::fragment));
    }
}
