package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.provider.RootPreferenceFragmentOfActivityProvider;

class RootPreferenceFragmentOfActivityProviderFactory {

    public static RootPreferenceFragmentOfActivityProvider createRootPreferenceFragmentOfActivityProvider(final List<ActivitySearchDatabaseConfig<? extends AppCompatActivity, ? extends Fragment, ? extends PreferenceFragmentCompat, ? extends PreferenceFragmentCompat>> activitySearchDatabaseConfigs) {
        final var rootPreferenceFragmentByActivity = getRootPreferenceFragmentByActivityMap(activitySearchDatabaseConfigs);
        return activityClass -> Optional.ofNullable(rootPreferenceFragmentByActivity.get(activityClass));
    }

    private static Map<? extends Class<? extends AppCompatActivity>, ? extends Class<? extends PreferenceFragmentCompat>> getRootPreferenceFragmentByActivityMap(
            final List<ActivitySearchDatabaseConfig<? extends AppCompatActivity, ? extends Fragment, ? extends PreferenceFragmentCompat, ? extends PreferenceFragmentCompat>> activitySearchDatabaseConfigs) {
        return activitySearchDatabaseConfigs
                .stream()
                .collect(
                        Collectors.toMap(
                                activitySearchDatabaseConfig -> activitySearchDatabaseConfig.activityWithRootPreferenceFragment().activityClass(),
                                activitySearchDatabaseConfig -> activitySearchDatabaseConfig.activityWithRootPreferenceFragment().rootPreferenceFragmentClass()));
    }
}
