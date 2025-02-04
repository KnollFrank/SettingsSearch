package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.Fragment2PreferenceFragmentConverter;
import de.KnollFrank.lib.settingssearch.common.Maps;

class Fragment2PreferenceFragmentConverterFactory {

    public static Fragment2PreferenceFragmentConverter createFragment2PreferenceFragmentConverter(final List<ActivitySearchDatabaseConfig<? extends AppCompatActivity, ? extends Fragment, ? extends PreferenceFragmentCompat, ? extends PreferenceFragmentCompat>> activitySearchDatabaseConfigs) {
        final var preferenceFragmentByFragment = getPreferenceFragmentByFragmentMap(activitySearchDatabaseConfigs);
        return fragment -> Maps.get(preferenceFragmentByFragment, fragment);
    }

    private static Map<Class<? extends Fragment>, Class<? extends PreferenceFragmentCompat>> getPreferenceFragmentByFragmentMap(final List<ActivitySearchDatabaseConfig<? extends AppCompatActivity, ? extends Fragment, ? extends PreferenceFragmentCompat, ? extends PreferenceFragmentCompat>> activitySearchDatabaseConfigs) {
        return activitySearchDatabaseConfigs
                .stream()
                .map(ActivitySearchDatabaseConfig::preferenceFragmentFactory)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(fragmentWithPreferenceFragmentConnectionPreferenceFragmentInitializer -> fragmentWithPreferenceFragmentConnectionPreferenceFragmentInitializer.fragmentWithPreferenceFragmentConnection)
                .collect(
                        Collectors.toMap(
                                FragmentWithPreferenceFragmentConnection::fragment,
                                FragmentWithPreferenceFragmentConnection::preferenceFragment));
    }
}
