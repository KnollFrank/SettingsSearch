package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import android.app.Activity;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.Fragment2PreferenceFragmentConverter;
import de.KnollFrank.lib.settingssearch.PreferenceWithHost;
import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.IFragments;
import de.KnollFrank.lib.settingssearch.provider.RootPreferenceFragmentOfActivityProvider;

class ActivitySearchDatabaseConfigs {

    private final List<ActivitySearchDatabaseConfig<? extends AppCompatActivity, ? extends Fragment, ? extends PreferenceFragmentCompat, ? extends PreferenceFragmentCompat>> activitySearchDatabaseConfigs;

    public ActivitySearchDatabaseConfigs(final List<ActivitySearchDatabaseConfig<? extends AppCompatActivity, ? extends Fragment, ? extends PreferenceFragmentCompat, ? extends PreferenceFragmentCompat>> activitySearchDatabaseConfigs) {
        this.activitySearchDatabaseConfigs = activitySearchDatabaseConfigs;
    }

    public FragmentFactory createFragmentFactory(final FragmentFactory delegate) {
        return new FragmentFactory() {

            @Override
            public <T extends Fragment> T instantiate(final Class<T> fragmentClass, final Optional<PreferenceWithHost> src, final Context context, final IFragments fragments) {
                return this
                        .createPreferenceFragment(fragmentClass, src, context, fragments)
                        .orElseGet(() -> delegate.instantiate(fragmentClass, src, context, fragments));
            }

            private <T extends Fragment> Optional<T> createPreferenceFragment(final Class<T> fragmentClass, final Optional<PreferenceWithHost> src, final Context context, final IFragments fragments) {
                return activitySearchDatabaseConfigs
                        .stream()
                        .map(ActivitySearchDatabaseConfig::preferenceFragmentFactory)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .map(preferenceFragmentFactory -> preferenceFragmentFactory.createPreferenceFragmentForClass(fragmentClass, src, context, fragments))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .findFirst();
            }
        };
    }

    public Fragment2PreferenceFragmentConverter createFragment2PreferenceFragmentConverter() {
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

    public RootPreferenceFragmentOfActivityProvider createRootPreferenceFragmentOfActivityProvider() {
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
