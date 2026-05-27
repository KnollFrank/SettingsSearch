package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import android.app.Activity;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.FragmentClassOfActivity;
import de.KnollFrank.lib.settingssearch.PreferenceScreenOfHostOfActivity;
import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.TreeProcessorFactory;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactories;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;
import de.KnollFrank.lib.settingssearch.graph.TreeBuilderListener;
import de.KnollFrank.lib.settingssearch.graph.TreeBuilderListeners;
import de.KnollFrank.lib.settingssearch.provider.ActivityInitializer;
import de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceFragmentConnectedToPreferenceProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceSearchablePredicate;
import de.KnollFrank.lib.settingssearch.provider.RootPreferenceFragmentOfActivityProvider;
import de.KnollFrank.lib.settingssearch.search.ReflectionIconResourceIdProvider;
import de.KnollFrank.lib.settingssearch.search.provider.BuiltinSearchableInfoProvider;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoProvider;

public class SearchDatabaseConfigBuilder<C> {

    private final FragmentClassOfActivity<? extends Fragment> rootFragment;
    private final TreeProcessorFactory<C> treeProcessorFactory;
    private FragmentFactory fragmentFactory = FragmentFactories.createWrappedDefaultFragmentFactory();
    private SearchableInfoProvider searchableInfoProvider = preference -> Optional.empty();
    private PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider = (preference, hostOfPreference) -> Optional.empty();
    private PreferenceFragmentConnectedToPreferenceProvider preferenceFragmentConnectedToPreferenceProvider = (preference, hostOfPreference) -> Optional.empty();
    private TreeBuilderListener<PreferenceScreenOfHostOfActivity, Preference> preferenceScreenTreeBuilderListener = TreeBuilderListeners.emptyTreeBuilderListener();
    private PreferenceSearchablePredicate preferenceSearchablePredicate = (preference, hostOfPreference) -> true;
    private ActivitySearchDatabaseConfigs activitySearchDatabaseConfigs = new ActivitySearchDatabaseConfigs(Map.of(), Set.of());
    private Map<Class<? extends Activity>, ActivityInitializer<?>> activityInitializerByActivity = Map.of();
    private FragmentIdProvider fragmentIdProvider = new DefaultFragmentIdProvider();
    private FragmentToPreferencesConverter fragmentToPreferencesConverter = fragment -> Optional.empty();

    SearchDatabaseConfigBuilder(final FragmentClassOfActivity<? extends Fragment> rootFragment,
                                final TreeProcessorFactory<C> treeProcessorFactory) {
        this.rootFragment = rootFragment;
        this.treeProcessorFactory = treeProcessorFactory;
    }

    @SuppressWarnings("unused")
    public SearchDatabaseConfigBuilder<C> withFragmentToPreferencesConverter(final FragmentToPreferencesConverter fragmentToPreferencesConverter) {
        this.fragmentToPreferencesConverter = fragmentToPreferencesConverter;
        return this;
    }

    @SuppressWarnings("unused")
    public SearchDatabaseConfigBuilder<C> withActivitySearchDatabaseConfigs(final ActivitySearchDatabaseConfigs activitySearchDatabaseConfigs) {
        this.activitySearchDatabaseConfigs = activitySearchDatabaseConfigs;
        return this;
    }

    @SuppressWarnings("unused")
    public SearchDatabaseConfigBuilder<C> withFragmentFactory(final de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.FragmentFactory fragmentFactory) {
        this.fragmentFactory = FragmentFactories.wrap(fragmentFactory);
        return this;
    }

    @SuppressWarnings("unused")
    public SearchDatabaseConfigBuilder<C> withSearchableInfoProvider(final SearchableInfoProvider searchableInfoProvider) {
        this.searchableInfoProvider = searchableInfoProvider;
        return this;
    }

    @SuppressWarnings("unused")
    public SearchDatabaseConfigBuilder<C> withPreferenceDialogAndSearchableInfoProvider(final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider) {
        this.preferenceDialogAndSearchableInfoProvider = preferenceDialogAndSearchableInfoProvider;
        return this;
    }

    @SuppressWarnings("unused")
    public SearchDatabaseConfigBuilder<C> withPreferenceFragmentConnectedToPreferenceProvider(final PreferenceFragmentConnectedToPreferenceProvider preferenceFragmentConnectedToPreferenceProvider) {
        this.preferenceFragmentConnectedToPreferenceProvider = preferenceFragmentConnectedToPreferenceProvider;
        return this;
    }


    @SuppressWarnings("unused")
    public SearchDatabaseConfigBuilder<C> withPreferenceScreenTreeBuilderListener(final TreeBuilderListener<PreferenceScreenOfHostOfActivity, Preference> preferenceScreenTreeBuilderListener) {
        this.preferenceScreenTreeBuilderListener = preferenceScreenTreeBuilderListener;
        return this;
    }

    @SuppressWarnings("unused")
    public SearchDatabaseConfigBuilder<C> withPreferenceSearchablePredicate(final PreferenceSearchablePredicate preferenceSearchablePredicate) {
        this.preferenceSearchablePredicate = preferenceSearchablePredicate;
        return this;
    }

    @SuppressWarnings("unused")
    public SearchDatabaseConfigBuilder<C> withActivityInitializerByActivity(final Map<Class<? extends Activity>, ActivityInitializer<?>> activityInitializerByActivity) {
        this.activityInitializerByActivity = activityInitializerByActivity;
        return this;
    }

    @SuppressWarnings("unused")
    public SearchDatabaseConfigBuilder<C> withFragmentIdProvider(final FragmentIdProvider fragmentIdProvider) {
        this.fragmentIdProvider = fragmentIdProvider;
        return this;
    }

    public SearchDatabaseConfig<C> build() {
        return new SearchDatabaseConfig<>(
                FragmentFactoryFactory.createFragmentFactory(
                        activitySearchDatabaseConfigs.principalAndProxies(),
                        fragmentFactory),
                new ReflectionIconResourceIdProvider(),
                searchableInfoProvider.orElse(new BuiltinSearchableInfoProvider()),
                preferenceDialogAndSearchableInfoProvider,
                preferenceFragmentConnectedToPreferenceProvider,
                rootFragment,
                createRootPreferenceFragmentOfActivityProvider(activitySearchDatabaseConfigs.rootPreferenceFragmentByActivity()),
                preferenceScreenTreeBuilderListener,
                preferenceSearchablePredicate,
                PrincipalAndProxyProviderFactory.createPrincipalAndProxyProvider(activitySearchDatabaseConfigs.principalAndProxies()),
                activityInitializerByActivity,
                fragmentIdProvider,
                treeProcessorFactory,
                fragmentToPreferencesConverter);
    }

    private static RootPreferenceFragmentOfActivityProvider createRootPreferenceFragmentOfActivityProvider(final Map<Class<? extends Activity>, Class<? extends PreferenceFragmentCompat>> rootPreferenceFragmentByActivity) {
        return new RootPreferenceFragmentOfActivityProvider() {

            @Override
            public Optional<Class<? extends PreferenceFragmentCompat>> getRootPreferenceFragmentOfActivity(final Class<? extends Activity> activityClass) {
                return Maps.get(rootPreferenceFragmentByActivity, activityClass);
            }
        };
    }
}
