package de.KnollFrank.lib.settingssearch.client;

import static de.KnollFrank.lib.settingssearch.fragment.Fragments.showFragment;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.FragmentManager;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;

import de.KnollFrank.lib.settingssearch.common.Utils;
import de.KnollFrank.lib.settingssearch.common.task.AsyncTaskWithProgressUpdateListeners;
import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunner;
import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunnerFactory;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentInitializer;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactoryAndInitializer;
import de.KnollFrank.lib.settingssearch.fragment.Fragments;
import de.KnollFrank.lib.settingssearch.fragment.factory.FragmentFactoryAndInitializerWithCache;
import de.KnollFrank.lib.settingssearch.search.MergedPreferenceScreenDataRepository;
import de.KnollFrank.lib.settingssearch.search.MergedPreferenceScreenDataRepositoryFactory;
import de.KnollFrank.lib.settingssearch.search.MergedPreferenceScreenFactory;
import de.KnollFrank.lib.settingssearch.search.SearchDatabaseDirectoryIO;
import de.KnollFrank.lib.settingssearch.search.SearchPreferenceFragment;
import de.KnollFrank.lib.settingssearch.search.progress.ProgressUpdateListener;

public class SearchPreferenceFragments implements MergedPreferenceScreenDataRepositoryFactory {

    public final SearchConfiguration searchConfiguration;
    private final SearchDatabaseConfig searchDatabaseConfig;
    private final SearchConfig searchConfig;
    private final FragmentManager fragmentManager;
    private final Locale locale;
    private final OnUiThreadRunner onUiThreadRunner;
    private final Context context;
    private final Supplier<Optional<AsyncTaskWithProgressUpdateListeners<?>>> createSearchDatabaseTaskSupplier;

    public static SearchPreferenceFragmentsBuilder builder(final SearchConfiguration searchConfiguration,
                                                           final FragmentManager fragmentManager,
                                                           final Activity activity) {
        return new SearchPreferenceFragmentsBuilder(
                searchConfiguration,
                fragmentManager,
                Utils.geCurrentLocale(activity.getResources()),
                OnUiThreadRunnerFactory.fromActivity(activity),
                activity);
    }

    protected SearchPreferenceFragments(final SearchConfiguration searchConfiguration,
                                        final SearchDatabaseConfig searchDatabaseConfig,
                                        final SearchConfig searchConfig,
                                        final FragmentManager fragmentManager,
                                        final Locale locale,
                                        final OnUiThreadRunner onUiThreadRunner,
                                        final Context context,
                                        final Supplier<Optional<AsyncTaskWithProgressUpdateListeners<?>>> createSearchDatabaseTaskSupplier) {
        this.searchConfiguration = searchConfiguration;
        this.searchDatabaseConfig = searchDatabaseConfig;
        this.searchConfig = searchConfig;
        this.fragmentManager = fragmentManager;
        this.locale = locale;
        this.onUiThreadRunner = onUiThreadRunner;
        this.context = context;
        this.createSearchDatabaseTaskSupplier = createSearchDatabaseTaskSupplier;
    }

    public void showSearchPreferenceFragment() {
        showFragment(
                new SearchPreferenceFragment(
                        searchConfiguration,
                        searchConfig.includePreferenceInSearchResultsPredicate(),
                        new MergedPreferenceScreenFactory(
                                searchConfig.showPreferencePathPredicate(),
                                searchConfig.prepareShow(),
                                searchConfiguration.fragmentContainerViewId(),
                                searchDatabaseConfig.fragmentFactory(),
                                searchConfig.markupsFactory(),
                                context,
                                locale,
                                onUiThreadRunner,
                                this,
                                searchConfig.searchResultsFragmentUI(),
                                searchConfig.searchResultsFilter(),
                                searchConfig.searchResultsSorter(),
                                searchConfig.preferencePathDisplayer()),
                        onUiThreadRunner,
                        createSearchDatabaseTaskSupplier,
                        searchConfig.searchPreferenceFragmentUI()),
                searchPreferenceFragment -> {
                },
                true,
                searchConfiguration.fragmentContainerViewId(),
                fragmentManager);
    }

    public void rebuildSearchDatabase() {
        new SearchDatabaseDirectoryIO(context).removeSearchDatabaseDirectories4AllLocales();
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
                                        searchDatabaseConfig.fragmentFactory(),
                                        preferenceDialogs)),
                        context));
    }

    @Override
    public MergedPreferenceScreenDataRepository createMergedPreferenceScreenDataRepository(
            final DefaultFragmentInitializer preferenceDialogs,
            final Context context,
            final ProgressUpdateListener progressUpdateListener,
            final Fragments fragments) {
        return new MergedPreferenceScreenDataRepository(
                fragments,
                preferenceDialogs,
                searchDatabaseConfig,
                searchConfiguration.rootPreferenceFragment(),
                progressUpdateListener,
                new SearchDatabaseDirectoryIO(context));
    }
}
