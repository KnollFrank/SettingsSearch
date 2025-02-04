package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.Fragment2PreferenceFragmentConverter;
import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.provider.RootPreferenceFragmentOfActivityProvider;

class ActivitySearchDatabaseConfigs {

    private final List<ActivitySearchDatabaseConfig<? extends AppCompatActivity, ? extends Fragment, ? extends PreferenceFragmentCompat, ? extends PreferenceFragmentCompat>> activitySearchDatabaseConfigs;

    public ActivitySearchDatabaseConfigs(final List<ActivitySearchDatabaseConfig<? extends AppCompatActivity, ? extends Fragment, ? extends PreferenceFragmentCompat, ? extends PreferenceFragmentCompat>> activitySearchDatabaseConfigs) {
        this.activitySearchDatabaseConfigs = activitySearchDatabaseConfigs;
    }

    public static Fragment2PreferenceFragmentConverter createFragment2PreferenceFragmentConverter(final List<ActivitySearchDatabaseConfig<? extends AppCompatActivity, ? extends Fragment, ? extends PreferenceFragmentCompat, ? extends PreferenceFragmentCompat>> activitySearchDatabaseConfigs) {
        return new Fragment2PreferenceFragmentConverter() {

            private final Map<Class<? extends Fragment>, Class<? extends PreferenceFragmentCompat>> preferenceFragmentByFragment = getPreferenceFragmentByFragmentMap(activitySearchDatabaseConfigs);

            @Override
            public Optional<Class<? extends PreferenceFragmentCompat>> asPreferenceFragment(final Class<? extends Fragment> fragment) {
                return Maps.get(preferenceFragmentByFragment, fragment);
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
        };
    }

    public static RootPreferenceFragmentOfActivityProvider createRootPreferenceFragmentOfActivityProvider(final List<ActivitySearchDatabaseConfig<? extends AppCompatActivity, ? extends Fragment, ? extends PreferenceFragmentCompat, ? extends PreferenceFragmentCompat>> activitySearchDatabaseConfigs) {
        return new RootPreferenceFragmentOfActivityProvider() {

            private final Map<? extends Class<? extends AppCompatActivity>, ? extends Class<? extends PreferenceFragmentCompat>> rootPreferenceFragmentByActivity = getRootPreferenceFragmentByActivityMap(activitySearchDatabaseConfigs);

            @Override
            public Optional<Class<? extends PreferenceFragmentCompat>> getRootPreferenceFragmentOfActivity(final Class<? extends Activity> activityClass) {
                return Optional.ofNullable(rootPreferenceFragmentByActivity.get(activityClass));
            }

            private static Map<? extends Class<? extends AppCompatActivity>, ? extends Class<? extends PreferenceFragmentCompat>> getRootPreferenceFragmentByActivityMap(final List<ActivitySearchDatabaseConfig<? extends AppCompatActivity, ? extends Fragment, ? extends PreferenceFragmentCompat, ? extends PreferenceFragmentCompat>> activitySearchDatabaseConfigs) {
                return activitySearchDatabaseConfigs
                        .stream()
                        .collect(
                                Collectors.toMap(
                                        activitySearchDatabaseConfig -> activitySearchDatabaseConfig.activityWithRootPreferenceFragment().activityClass(),
                                        activitySearchDatabaseConfig -> activitySearchDatabaseConfig.activityWithRootPreferenceFragment().rootPreferenceFragmentClass()));
            }
        };
    }
}
