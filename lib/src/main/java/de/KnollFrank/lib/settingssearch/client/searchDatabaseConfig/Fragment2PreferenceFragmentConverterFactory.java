package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Map;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.Fragment2PreferenceFragmentConverter;
import de.KnollFrank.lib.settingssearch.common.Maps;

class Fragment2PreferenceFragmentConverterFactory {

    public static Fragment2PreferenceFragmentConverter createFragment2PreferenceFragmentConverter(final ActivitySearchDatabaseConfigs activitySearchDatabaseConfigs) {
        final var preferenceFragmentByFragment = getPreferenceFragmentByFragmentMap(activitySearchDatabaseConfigs);
        return fragment -> Maps.get(preferenceFragmentByFragment, fragment);
    }

    private static Map<Class<? extends Fragment>, Class<? extends PreferenceFragmentCompat>> getPreferenceFragmentByFragmentMap(final ActivitySearchDatabaseConfigs activitySearchDatabaseConfigs) {
        return activitySearchDatabaseConfigs
                .preferenceFragmentFactories()
                .stream()
                .map(preferenceFragmentFactory -> preferenceFragmentFactory.fragmentWithPreferenceFragmentConnection)
                .collect(
                        Collectors.toMap(
                                FragmentWithPreferenceFragmentConnection::fragment,
                                FragmentWithPreferenceFragmentConnection::preferenceFragment));
    }
}
