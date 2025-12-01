package de.KnollFrank.lib.settingssearch.client;

import static de.KnollFrank.lib.settingssearch.fragment.Fragments.showFragment;

import android.os.PersistableBundle;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import de.KnollFrank.lib.settingssearch.MergedPreferenceScreen;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.SearchDatabaseConfig;
import de.KnollFrank.lib.settingssearch.common.Locales;
import de.KnollFrank.lib.settingssearch.common.task.AsyncTaskWithProgressUpdateListeners;
import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunner;
import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunnerFactory;
import de.KnollFrank.lib.settingssearch.db.preference.db.DAOProvider;
import de.KnollFrank.lib.settingssearch.db.preference.db.DatabaseResetter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.ConfigurationBundleConverter;
import de.KnollFrank.lib.settingssearch.fragment.FragmentInitializer;
import de.KnollFrank.lib.settingssearch.fragment.Fragments;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.PreferenceDialogs;
import de.KnollFrank.lib.settingssearch.search.MergedPreferenceScreenDataRepository;
import de.KnollFrank.lib.settingssearch.search.MergedPreferenceScreenDataRepositoryFactory;
import de.KnollFrank.lib.settingssearch.search.MergedPreferenceScreenDataRepositoryProvider;
import de.KnollFrank.lib.settingssearch.search.MergedPreferenceScreenFactory;
import de.KnollFrank.lib.settingssearch.search.PreferenceMatcher;
import de.KnollFrank.lib.settingssearch.search.SearchPreferenceFragment;
import de.KnollFrank.lib.settingssearch.search.progress.ProgressUpdateListener;

public class SearchPreferenceFragments<C> implements MergedPreferenceScreenDataRepositoryProvider<C> {

    public final SearchConfig searchConfig;
    public final SearchDatabaseConfig searchDatabaseConfig;
    private final Locale locale;
    private final OnUiThreadRunner onUiThreadRunner;
    private final FragmentActivity activity;
    private final Supplier<Optional<AsyncTaskWithProgressUpdateListeners<Void, DAOProvider<C>>>> createSearchDatabaseTaskSupplier;
    private final Consumer<MergedPreferenceScreen<C>> onMergedPreferenceScreenAvailable;
    private final DAOProvider<C> daoProvider;
    private final PersistableBundle configuration;
    private final ConfigurationBundleConverter<C> configurationBundleConverter;

    public static <C> SearchPreferenceFragmentsBuilder<C> builder(final SearchDatabaseConfig searchDatabaseConfig,
                                                                  final SearchConfig searchConfig,
                                                                  final FragmentActivity activity,
                                                                  final DAOProvider<C> daoProvider,
                                                                  final PersistableBundle configuration,
                                                                  final ConfigurationBundleConverter<C> configurationBundleConverter) {
        return new SearchPreferenceFragmentsBuilder<>(
                searchDatabaseConfig,
                searchConfig,
                Locales.getCurrentLanguageLocale(activity.getResources()),
                OnUiThreadRunnerFactory.fromActivity(activity),
                activity,
                daoProvider,
                configuration,
                configurationBundleConverter);
    }

    protected SearchPreferenceFragments(final SearchDatabaseConfig searchDatabaseConfig,
                                        final SearchConfig searchConfig,
                                        final Locale locale,
                                        final OnUiThreadRunner onUiThreadRunner,
                                        final FragmentActivity activity,
                                        final Supplier<Optional<AsyncTaskWithProgressUpdateListeners<Void, DAOProvider<C>>>> createSearchDatabaseTaskSupplier,
                                        final Consumer<MergedPreferenceScreen<C>> onMergedPreferenceScreenAvailable,
                                        final DAOProvider<C> daoProvider,
                                        final PersistableBundle configuration,
                                        final ConfigurationBundleConverter<C> configurationBundleConverter) {
        this.searchDatabaseConfig = searchDatabaseConfig;
        this.searchConfig = searchConfig;
        this.locale = locale;
        this.onUiThreadRunner = onUiThreadRunner;
        this.activity = activity;
        this.createSearchDatabaseTaskSupplier = createSearchDatabaseTaskSupplier;
        this.onMergedPreferenceScreenAvailable = onMergedPreferenceScreenAvailable;
        this.daoProvider = daoProvider;
        this.configuration = configuration;
        this.configurationBundleConverter = configurationBundleConverter;
    }

    public void showSearchPreferenceFragment() {
        showFragment(
                new SearchPreferenceFragment<>(
                        searchConfig.queryHint,
                        searchConfig.searchResultsFilter,
                        new PreferenceMatcher(searchConfig.stringMatcher),
                        getMergedPreferenceScreenFactory(),
                        onUiThreadRunner,
                        createSearchDatabaseTaskSupplier,
                        searchConfig.searchPreferenceFragmentUI,
                        onMergedPreferenceScreenAvailable,
                        locale,
                        configuration),
                searchPreferenceFragment -> {
                },
                true,
                searchConfig.fragmentContainerViewId,
                Optional.of(getSearchPreferenceFragmentTag()),
                activity.getSupportFragmentManager());
    }

    public static void hideSearchPreferenceFragment(final FragmentManager fragmentManager) {
        SearchPreferenceFragments
                .findSearchPreferenceFragment(fragmentManager)
                .ifPresent(searchPreferenceFragment -> Fragments.hideFragment(searchPreferenceFragment, fragmentManager));
    }

    public MergedPreferenceScreenFactory<C> getMergedPreferenceScreenFactory() {
        return new MergedPreferenceScreenFactory<>(
                searchConfig.showPreferencePathPredicate,
                searchConfig.prepareShow,
                searchDatabaseConfig.fragmentFactory,
                searchConfig.markupsFactory,
                activity,
                locale,
                onUiThreadRunner,
                this,
                searchConfig.searchResultsFragmentUI,
                searchConfig.searchResultsSorter,
                searchConfig.preferencePathDisplayer,
                searchDatabaseConfig.activityInitializerByActivity,
                searchDatabaseConfig.principalAndProxyProvider,
                searchConfig.showSettingsFragmentAndHighlightSetting,
                daoProvider,
                searchDatabaseConfig.preferenceSearchablePredicate);
    }

    public void rebuildSearchDatabase() {
        DatabaseResetter.resetDatabase(daoProvider);
    }

    @Override
    public MergedPreferenceScreenDataRepository<C> createMergedPreferenceScreenDataRepository(
            final FragmentInitializer fragmentInitializer,
            final PreferenceDialogs preferenceDialogs,
            final FragmentActivity activityContext,
            final DAOProvider<C> daoProvider,
            final ProgressUpdateListener progressUpdateListener) {
        return createMergedPreferenceScreenDataRepository(
                preferenceDialogs,
                activityContext,
                daoProvider,
                progressUpdateListener,
                InstantiateAndInitializeFragmentFactory.createInstantiateAndInitializeFragment(
                        searchDatabaseConfig.fragmentFactory,
                        fragmentInitializer,
                        activityContext));
    }

    @Override
    public MergedPreferenceScreenDataRepository<C> createMergedPreferenceScreenDataRepository(
            final PreferenceDialogs preferenceDialogs,
            final FragmentActivity activityContext,
            final DAOProvider<C> daoProvider,
            final ProgressUpdateListener progressUpdateListener,
            final InstantiateAndInitializeFragment instantiateAndInitializeFragment) {
        return MergedPreferenceScreenDataRepositoryFactory.createMergedPreferenceScreenDataRepository(
                instantiateAndInitializeFragment,
                preferenceDialogs,
                searchDatabaseConfig,
                progressUpdateListener,
                activityContext,
                daoProvider,
                locale,
                configurationBundleConverter);
    }

    private static <C> Optional<SearchPreferenceFragment<C>> findSearchPreferenceFragment(final FragmentManager fragmentManager) {
        return Optional.ofNullable((SearchPreferenceFragment<C>) fragmentManager.findFragmentByTag(getSearchPreferenceFragmentTag()));
    }

    private static String getSearchPreferenceFragmentTag() {
        return SearchPreferenceFragment.class.getSimpleName();
    }
}
