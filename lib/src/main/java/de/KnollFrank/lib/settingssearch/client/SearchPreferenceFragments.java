package de.KnollFrank.lib.settingssearch.client;

import static de.KnollFrank.lib.settingssearch.fragment.Fragments.showFragment;

import android.content.Context;
import android.os.PersistableBundle;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import de.KnollFrank.lib.settingssearch.MergedPreferenceScreen;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.SearchDatabaseConfig;
import de.KnollFrank.lib.settingssearch.common.Utils;
import de.KnollFrank.lib.settingssearch.common.task.AsyncTaskWithProgressUpdateListeners;
import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunner;
import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunnerFactory;
import de.KnollFrank.lib.settingssearch.db.preference.db.DAOProvider;
import de.KnollFrank.lib.settingssearch.db.preference.db.DatabaseResetter;
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

public class SearchPreferenceFragments implements MergedPreferenceScreenDataRepositoryProvider {

    public final SearchConfig searchConfig;
    private final SearchDatabaseConfig searchDatabaseConfig;
    private final Locale locale;
    private final OnUiThreadRunner onUiThreadRunner;
    private final FragmentActivity activity;
    private final Supplier<Optional<AsyncTaskWithProgressUpdateListeners<Void, DAOProvider>>> createSearchDatabaseTaskSupplier;
    private final Consumer<MergedPreferenceScreen> onMergedPreferenceScreenAvailable;
    private final DAOProvider daoProvider;
    private final PersistableBundle configuration;

    public static SearchPreferenceFragmentsBuilder builder(final SearchDatabaseConfig searchDatabaseConfig,
                                                           final SearchConfig searchConfig,
                                                           final FragmentActivity activity,
                                                           final DAOProvider daoProvider,
                                                           final PersistableBundle configuration) {
        return new SearchPreferenceFragmentsBuilder(
                searchDatabaseConfig,
                searchConfig,
                Utils.getCurrentLanguageLocale(activity.getResources()),
                OnUiThreadRunnerFactory.fromActivity(activity),
                activity,
                daoProvider,
                configuration);
    }

    protected SearchPreferenceFragments(final SearchDatabaseConfig searchDatabaseConfig,
                                        final SearchConfig searchConfig,
                                        final Locale locale,
                                        final OnUiThreadRunner onUiThreadRunner,
                                        final FragmentActivity activity,
                                        final Supplier<Optional<AsyncTaskWithProgressUpdateListeners<Void, DAOProvider>>> createSearchDatabaseTaskSupplier,
                                        final Consumer<MergedPreferenceScreen> onMergedPreferenceScreenAvailable,
                                        final DAOProvider daoProvider,
                                        final PersistableBundle configuration) {
        this.searchDatabaseConfig = searchDatabaseConfig;
        this.searchConfig = searchConfig;
        this.locale = locale;
        this.onUiThreadRunner = onUiThreadRunner;
        this.activity = activity;
        this.createSearchDatabaseTaskSupplier = createSearchDatabaseTaskSupplier;
        this.onMergedPreferenceScreenAvailable = onMergedPreferenceScreenAvailable;
        this.daoProvider = daoProvider;
        this.configuration = configuration;
    }

    public void showSearchPreferenceFragment() {
        showFragment(
                new SearchPreferenceFragment(
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

    public MergedPreferenceScreenFactory getMergedPreferenceScreenFactory() {
        return new MergedPreferenceScreenFactory(
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
                daoProvider);
    }

    public void rebuildSearchDatabase() {
        DatabaseResetter.resetDatabase(daoProvider);
    }

    @Override
    public MergedPreferenceScreenDataRepository createMergedPreferenceScreenDataRepository(
            final FragmentInitializer fragmentInitializer,
            final PreferenceDialogs preferenceDialogs,
            final Context context,
            final DAOProvider daoProvider,
            final ProgressUpdateListener progressUpdateListener) {
        return createMergedPreferenceScreenDataRepository(
                preferenceDialogs,
                context,
                daoProvider,
                progressUpdateListener,
                InstantiateAndInitializeFragmentFactory.createInstantiateAndInitializeFragment(
                        searchDatabaseConfig.fragmentFactory,
                        fragmentInitializer,
                        context));
    }

    @Override
    public MergedPreferenceScreenDataRepository createMergedPreferenceScreenDataRepository(
            final PreferenceDialogs preferenceDialogs,
            final Context context,
            final DAOProvider daoProvider,
            final ProgressUpdateListener progressUpdateListener,
            final InstantiateAndInitializeFragment instantiateAndInitializeFragment) {
        return MergedPreferenceScreenDataRepositoryFactory.createMergedPreferenceScreenDataRepository(
                instantiateAndInitializeFragment,
                preferenceDialogs,
                searchDatabaseConfig,
                progressUpdateListener,
                context,
                daoProvider,
                locale);
    }

    private static Optional<SearchPreferenceFragment> findSearchPreferenceFragment(final FragmentManager fragmentManager) {
        return Optional.ofNullable((SearchPreferenceFragment) fragmentManager.findFragmentByTag(getSearchPreferenceFragmentTag()));
    }

    private static String getSearchPreferenceFragmentTag() {
        return SearchPreferenceFragment.class.getSimpleName();
    }
}
