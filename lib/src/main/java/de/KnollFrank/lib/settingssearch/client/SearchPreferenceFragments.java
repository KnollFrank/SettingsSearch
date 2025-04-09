package de.KnollFrank.lib.settingssearch.client;

import static de.KnollFrank.lib.settingssearch.fragment.Fragments.showFragment;

import android.content.Context;

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
import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchDatabaseDirectoryIO;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentInitializer;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactoryAndInitializer;
import de.KnollFrank.lib.settingssearch.fragment.Fragments;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;
import de.KnollFrank.lib.settingssearch.fragment.factory.FragmentFactoryAndInitializerWithCache;
import de.KnollFrank.lib.settingssearch.search.MergedPreferenceScreenDataRepository;
import de.KnollFrank.lib.settingssearch.search.MergedPreferenceScreenDataRepositoryFactory;
import de.KnollFrank.lib.settingssearch.search.MergedPreferenceScreenFactory;
import de.KnollFrank.lib.settingssearch.search.SearchPreferenceFragment;
import de.KnollFrank.lib.settingssearch.search.progress.ProgressUpdateListener;

public class SearchPreferenceFragments implements MergedPreferenceScreenDataRepositoryFactory {

    public final SearchConfig searchConfig;
    private final SearchDatabaseConfig searchDatabaseConfig;
    private final Locale locale;
    private final OnUiThreadRunner onUiThreadRunner;
    private final FragmentActivity activity;
    private final Supplier<Optional<AsyncTaskWithProgressUpdateListeners<?>>> createSearchDatabaseTaskSupplier;
    private final Consumer<MergedPreferenceScreen> onMergedPreferenceScreenAvailable;

    public static SearchPreferenceFragmentsBuilder builder(final SearchDatabaseConfig searchDatabaseConfig,
                                                           final SearchConfig searchConfig,
                                                           final FragmentActivity activity) {
        return new SearchPreferenceFragmentsBuilder(
                searchDatabaseConfig,
                searchConfig,
                Utils.geCurrentLocale(activity.getResources()),
                OnUiThreadRunnerFactory.fromActivity(activity),
                activity);
    }

    protected SearchPreferenceFragments(final SearchDatabaseConfig searchDatabaseConfig,
                                        final SearchConfig searchConfig,
                                        final Locale locale,
                                        final OnUiThreadRunner onUiThreadRunner,
                                        final FragmentActivity activity,
                                        final Supplier<Optional<AsyncTaskWithProgressUpdateListeners<?>>> createSearchDatabaseTaskSupplier,
                                        final Consumer<MergedPreferenceScreen> onMergedPreferenceScreenAvailable) {
        this.searchDatabaseConfig = searchDatabaseConfig;
        this.searchConfig = searchConfig;
        this.locale = locale;
        this.onUiThreadRunner = onUiThreadRunner;
        this.activity = activity;
        this.createSearchDatabaseTaskSupplier = createSearchDatabaseTaskSupplier;
        this.onMergedPreferenceScreenAvailable = onMergedPreferenceScreenAvailable;
    }

    public void showSearchPreferenceFragment() {
        showFragment(
                new SearchPreferenceFragment(
                        searchConfig.queryHint,
                        searchConfig.includePreferenceInSearchResultsPredicate,
                        getMergedPreferenceScreenFactory(),
                        onUiThreadRunner,
                        createSearchDatabaseTaskSupplier,
                        searchConfig.searchPreferenceFragmentUI,
                        onMergedPreferenceScreenAvailable),
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
                searchConfig.searchResultsFilter,
                searchConfig.searchResultsSorter,
                searchConfig.preferencePathDisplayer,
                searchDatabaseConfig.activityInitializerByActivity,
                searchDatabaseConfig.principalAndProxyProvider,
                searchConfig.showSettingsFragmentAndHighlightSetting);
    }

    // FK-TODO: provide the possibility to only update parts of the search database (or graph) which need to be changed instead of deleting and rebuilding the whole database.
    public void rebuildSearchDatabase() {
        new SearchDatabaseDirectoryIO(activity).removeSearchDatabaseDirectories4AllLocales();
    }

    @Override
    public MergedPreferenceScreenDataRepository createMergedPreferenceScreenDataRepository(
            final DefaultFragmentInitializer preferenceDialogs,
            final Context context,
            final ProgressUpdateListener progressUpdateListener) {
        return createMergedPreferenceScreenDataRepository(
                preferenceDialogs,
                context,
                progressUpdateListener,
                new Fragments(
                        new FragmentFactoryAndInitializerWithCache(
                                new FragmentFactoryAndInitializer(
                                        searchDatabaseConfig.fragmentFactory,
                                        preferenceDialogs)),
                        context));
    }

    @Override
    public MergedPreferenceScreenDataRepository createMergedPreferenceScreenDataRepository(
            final DefaultFragmentInitializer preferenceDialogs,
            final Context context,
            final ProgressUpdateListener progressUpdateListener,
            final InstantiateAndInitializeFragment instantiateAndInitializeFragment) {
        return new MergedPreferenceScreenDataRepository(
                instantiateAndInitializeFragment,
                preferenceDialogs,
                searchDatabaseConfig,
                progressUpdateListener,
                new SearchDatabaseDirectoryIO(context),
                searchDatabaseConfig.principalAndProxyProvider,
                context);
    }

    private static Optional<SearchPreferenceFragment> findSearchPreferenceFragment(final FragmentManager fragmentManager) {
        return Optional.ofNullable((SearchPreferenceFragment) fragmentManager.findFragmentByTag(getSearchPreferenceFragmentTag()));
    }

    private static String getSearchPreferenceFragmentTag() {
        return SearchPreferenceFragment.class.getSimpleName();
    }
}
