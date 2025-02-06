package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;
import de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceFragmentConnected2PreferenceProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceScreenGraphAvailableListener;
import de.KnollFrank.lib.settingssearch.provider.PreferenceSearchablePredicate;
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
    private ActivitySearchDatabaseConfigs activitySearchDatabaseConfigs = new ActivitySearchDatabaseConfigs(Set.of(), Set.of());

    public SearchDatabaseConfigBuilder withActivitySearchDatabaseConfigs(final ActivitySearchDatabaseConfigs activitySearchDatabaseConfigs) {
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
                new de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.FragmentFactory(
                        activitySearchDatabaseConfigs.preferenceFragmentFactories(),
                        fragmentFactory),
                iconResourceIdProvider,
                searchableInfoProvider.orElse(new BuiltinSearchableInfoProvider()),
                preferenceDialogAndSearchableInfoProvider,
                preferenceFragmentConnected2PreferenceProvider,
                RootPreferenceFragmentOfActivityProviderFactory.createRootPreferenceFragmentOfActivityProvider(activitySearchDatabaseConfigs),
                preferenceScreenGraphAvailableListener,
                preferenceSearchablePredicate,
                Fragment2PreferenceFragmentConverterFactory.createFragment2PreferenceFragmentConverter(activitySearchDatabaseConfigs));
    }
}
