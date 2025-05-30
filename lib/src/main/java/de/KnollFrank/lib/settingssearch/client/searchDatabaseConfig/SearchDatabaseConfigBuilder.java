package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import android.app.Activity;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;
import de.KnollFrank.lib.settingssearch.graph.ComputePreferencesListener;
import de.KnollFrank.lib.settingssearch.provider.ActivityInitializer;
import de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceFragmentConnected2PreferenceProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceScreenGraphAvailableListener;
import de.KnollFrank.lib.settingssearch.provider.PreferenceSearchablePredicate;
import de.KnollFrank.lib.settingssearch.provider.RootPreferenceFragmentOfActivityProvider;
import de.KnollFrank.lib.settingssearch.search.ReflectionIconResourceIdProvider;
import de.KnollFrank.lib.settingssearch.search.provider.BuiltinSearchableInfoProvider;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoProvider;

public class SearchDatabaseConfigBuilder {

    private final Class<? extends PreferenceFragmentCompat> rootPreferenceFragment;
    private FragmentFactory fragmentFactory = new DefaultFragmentFactory();
    private SearchableInfoProvider searchableInfoProvider = preference -> Optional.empty();
    private PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider = (preference, hostOfPreference) -> Optional.empty();
    private PreferenceFragmentConnected2PreferenceProvider preferenceFragmentConnected2PreferenceProvider = (preference, hostOfPreference) -> Optional.empty();
    private PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener = preferenceScreenGraph -> {
    };
    private ComputePreferencesListener computePreferencesListener =
            new ComputePreferencesListener() {

                @Override
                public void onStartComputePreferences() {
                }

                @Override
                public void onFinishComputePreferences() {
                }
            };
    private PreferenceSearchablePredicate preferenceSearchablePredicate = (preference, hostOfPreference) -> true;
    private ActivitySearchDatabaseConfigs activitySearchDatabaseConfigs = new ActivitySearchDatabaseConfigs(Map.of(), Set.of());
    private Map<Class<? extends Activity>, ActivityInitializer<?>> activityInitializerByActivity = Map.of();
    private PreferenceFragmentIdProvider preferenceFragmentIdProvider = new DefaultPreferenceFragmentIdProvider();

    SearchDatabaseConfigBuilder(final Class<? extends PreferenceFragmentCompat> rootPreferenceFragment) {
        this.rootPreferenceFragment = rootPreferenceFragment;
    }

    @SuppressWarnings("unused")
    public SearchDatabaseConfigBuilder withActivitySearchDatabaseConfigs(final ActivitySearchDatabaseConfigs activitySearchDatabaseConfigs) {
        this.activitySearchDatabaseConfigs = activitySearchDatabaseConfigs;
        return this;
    }

    @SuppressWarnings("unused")
    public SearchDatabaseConfigBuilder withFragmentFactory(final FragmentFactory fragmentFactory) {
        this.fragmentFactory = fragmentFactory;
        return this;
    }

    @SuppressWarnings("unused")
    public SearchDatabaseConfigBuilder withSearchableInfoProvider(final SearchableInfoProvider searchableInfoProvider) {
        this.searchableInfoProvider = searchableInfoProvider;
        return this;
    }

    @SuppressWarnings("unused")
    public SearchDatabaseConfigBuilder withPreferenceDialogAndSearchableInfoProvider(final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider) {
        this.preferenceDialogAndSearchableInfoProvider = preferenceDialogAndSearchableInfoProvider;
        return this;
    }

    @SuppressWarnings("unused")
    public SearchDatabaseConfigBuilder withPreferenceFragmentConnected2PreferenceProvider(final PreferenceFragmentConnected2PreferenceProvider preferenceFragmentConnected2PreferenceProvider) {
        this.preferenceFragmentConnected2PreferenceProvider = preferenceFragmentConnected2PreferenceProvider;
        return this;
    }


    @SuppressWarnings("unused")
    public SearchDatabaseConfigBuilder withPreferenceScreenGraphAvailableListener(final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener) {
        this.preferenceScreenGraphAvailableListener = preferenceScreenGraphAvailableListener;
        return this;
    }

    @SuppressWarnings("unused")
    public SearchDatabaseConfigBuilder withComputePreferencesListener(final ComputePreferencesListener computePreferencesListener) {
        this.computePreferencesListener = computePreferencesListener;
        return this;
    }

    @SuppressWarnings("unused")
    public SearchDatabaseConfigBuilder withPreferenceSearchablePredicate(final PreferenceSearchablePredicate preferenceSearchablePredicate) {
        this.preferenceSearchablePredicate = preferenceSearchablePredicate;
        return this;
    }

    @SuppressWarnings("unused")
    public SearchDatabaseConfigBuilder withActivityInitializerByActivity(final Map<Class<? extends Activity>, ActivityInitializer<?>> activityInitializerByActivity) {
        this.activityInitializerByActivity = activityInitializerByActivity;
        return this;
    }

    @SuppressWarnings("unused")
    public SearchDatabaseConfigBuilder withPreferenceFragmentIdProvider(final PreferenceFragmentIdProvider preferenceFragmentIdProvider) {
        this.preferenceFragmentIdProvider = preferenceFragmentIdProvider;
        return this;
    }

    public SearchDatabaseConfig build() {
        return new SearchDatabaseConfig(
                FragmentFactoryFactory.createFragmentFactory(activitySearchDatabaseConfigs.principalAndProxies(), fragmentFactory),
                new ReflectionIconResourceIdProvider(),
                searchableInfoProvider.orElse(new BuiltinSearchableInfoProvider()),
                preferenceDialogAndSearchableInfoProvider,
                preferenceFragmentConnected2PreferenceProvider,
                rootPreferenceFragment,
                new RootPreferenceFragmentOfActivityProvider() {

                    @Override
                    public Optional<Class<? extends PreferenceFragmentCompat>> getRootPreferenceFragmentOfActivity(final Class<? extends Activity> activityClass) {
                        return Maps.get(activitySearchDatabaseConfigs.rootPreferenceFragmentByActivity(), activityClass);
                    }
                },
                preferenceScreenGraphAvailableListener,
                computePreferencesListener,
                preferenceSearchablePredicate,
                PrincipalAndProxyProviderFactory.createPrincipalAndProxyProvider(activitySearchDatabaseConfigs.principalAndProxies()),
                activityInitializerByActivity,
                preferenceFragmentIdProvider);
    }
}
