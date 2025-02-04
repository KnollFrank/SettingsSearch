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
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.IFragments;
import de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceFragmentConnected2PreferenceProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceScreenGraphAvailableListener;
import de.KnollFrank.lib.settingssearch.provider.PreferenceSearchablePredicate;
import de.KnollFrank.lib.settingssearch.provider.RootPreferenceFragmentOfActivityProvider;
import de.KnollFrank.lib.settingssearch.search.ReflectionIconResourceIdProvider;
import de.KnollFrank.lib.settingssearch.search.provider.BuiltinSearchableInfoProvider;
import de.KnollFrank.lib.settingssearch.search.provider.IconResourceIdProvider;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoProvider;

public class SearchDatabaseConfigBuilder {

    private FragmentFactory fragmentFactory = new DefaultFragmentFactory();
    private IconResourceIdProvider iconResourceIdProvider = new ReflectionIconResourceIdProvider();
    private SearchableInfoProvider searchableInfoProvider = preference -> Optional.empty();
    private PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider = (preference, hostOfPreference) -> Optional.empty();
    private PreferenceFragmentConnected2PreferenceProvider preferenceFragmentConnected2PreferenceProvider = (preference, hostOfPreference) -> Optional.empty();
    private PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener = preferenceScreenGraph -> {
    };
    private PreferenceSearchablePredicate preferenceSearchablePredicate = (preference, hostOfPreference) -> true;
    private List<ActivitySearchDatabaseConfig<? extends AppCompatActivity, ? extends Fragment, ? extends PreferenceFragmentCompat, ? extends PreferenceFragmentCompat>> activitySearchDatabaseConfigs = List.of();

    public SearchDatabaseConfigBuilder withActivitySearchDatabaseConfigs(final List<ActivitySearchDatabaseConfig<? extends AppCompatActivity, ? extends Fragment, ? extends PreferenceFragmentCompat, ? extends PreferenceFragmentCompat>> activitySearchDatabaseConfigs) {
        this.activitySearchDatabaseConfigs = activitySearchDatabaseConfigs;
        return this;
    }

    public SearchDatabaseConfigBuilder withFragmentFactory(final FragmentFactory fragmentFactory) {
        this.fragmentFactory = fragmentFactory;
        return this;
    }

    private SearchDatabaseConfigBuilder withIconResourceIdProvider(final IconResourceIdProvider iconResourceIdProvider) {
        this.iconResourceIdProvider = iconResourceIdProvider;
        return this;
    }

    public SearchDatabaseConfigBuilder withSearchableInfoProvider(final SearchableInfoProvider searchableInfoProvider) {
        this.searchableInfoProvider = searchableInfoProvider;
        return this;
    }

    public SearchDatabaseConfigBuilder withPreferenceDialogAndSearchableInfoProvider(final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider) {
        this.preferenceDialogAndSearchableInfoProvider = preferenceDialogAndSearchableInfoProvider;
        return this;
    }

    public SearchDatabaseConfigBuilder withPreferenceFragmentConnected2PreferenceProvider(final PreferenceFragmentConnected2PreferenceProvider preferenceFragmentConnected2PreferenceProvider) {
        this.preferenceFragmentConnected2PreferenceProvider = preferenceFragmentConnected2PreferenceProvider;
        return this;
    }


    public SearchDatabaseConfigBuilder withPreferenceScreenGraphAvailableListener(final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener) {
        this.preferenceScreenGraphAvailableListener = preferenceScreenGraphAvailableListener;
        return this;
    }

    public SearchDatabaseConfigBuilder withPreferenceSearchablePredicate(final PreferenceSearchablePredicate preferenceSearchablePredicate) {
        this.preferenceSearchablePredicate = preferenceSearchablePredicate;
        return this;
    }

    public SearchDatabaseConfig build() {
        return new SearchDatabaseConfig(
                createFragmentFactory(activitySearchDatabaseConfigs, fragmentFactory),
                iconResourceIdProvider,
                searchableInfoProvider.orElse(new BuiltinSearchableInfoProvider()),
                preferenceDialogAndSearchableInfoProvider,
                preferenceFragmentConnected2PreferenceProvider,
                createRootPreferenceFragmentOfActivityProvider(activitySearchDatabaseConfigs),
                preferenceScreenGraphAvailableListener,
                preferenceSearchablePredicate,
                createFragment2PreferenceFragmentConverter(activitySearchDatabaseConfigs));
    }

    // FK-TODO: move to new class ActivitySearchDatabaseConfigs
    private static FragmentFactory createFragmentFactory(
            final List<ActivitySearchDatabaseConfig<? extends AppCompatActivity, ? extends Fragment, ? extends PreferenceFragmentCompat, ? extends PreferenceFragmentCompat>> activitySearchDatabaseConfigs,
            final FragmentFactory delegate) {
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
                        .map(ActivitySearchDatabaseConfig::fragmentWithPreferenceFragmentConnection_preferenceFragmentInitializer)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .filter(fragmentWithPreferenceFragmentConnection_PreferenceFragmentInitializer -> fragmentWithPreferenceFragmentConnection_PreferenceFragmentInitializer.canCreatePreferenceFragmentHavingClass(fragmentClass))
                        .findFirst()
                        .map(fragmentWithPreferenceFragmentConnection_PreferenceFragmentInitializer -> (T) fragmentWithPreferenceFragmentConnection_PreferenceFragmentInitializer.createPreferenceFragment(src, context, fragments));
            }
        };
    }

    // FK-TODO: move to new class ActivitySearchDatabaseConfigs
    private static Fragment2PreferenceFragmentConverter createFragment2PreferenceFragmentConverter(final List<ActivitySearchDatabaseConfig<? extends AppCompatActivity, ? extends Fragment, ? extends PreferenceFragmentCompat, ? extends PreferenceFragmentCompat>> activitySearchDatabaseConfigs) {
        return new Fragment2PreferenceFragmentConverter() {

            private final Map<Class<? extends Fragment>, Class<? extends PreferenceFragmentCompat>> preferenceFragmentByFragment = getPreferenceFragmentByFragmentMap(activitySearchDatabaseConfigs);

            @Override
            public Optional<Class<? extends PreferenceFragmentCompat>> asPreferenceFragment(final Class<? extends Fragment> fragment) {
                return Maps.get(preferenceFragmentByFragment, fragment);
            }

            private static Map<Class<? extends Fragment>, Class<? extends PreferenceFragmentCompat>> getPreferenceFragmentByFragmentMap(final List<ActivitySearchDatabaseConfig<? extends AppCompatActivity, ? extends Fragment, ? extends PreferenceFragmentCompat, ? extends PreferenceFragmentCompat>> activitySearchDatabaseConfigs) {
                return activitySearchDatabaseConfigs
                        .stream()
                        .map(ActivitySearchDatabaseConfig::fragmentWithPreferenceFragmentConnection_preferenceFragmentInitializer)
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

    // FK-TODO: move to new class ActivitySearchDatabaseConfigs
    private static RootPreferenceFragmentOfActivityProvider createRootPreferenceFragmentOfActivityProvider(final List<ActivitySearchDatabaseConfig<? extends AppCompatActivity, ? extends Fragment, ? extends PreferenceFragmentCompat, ? extends PreferenceFragmentCompat>> activitySearchDatabaseConfigs) {
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
