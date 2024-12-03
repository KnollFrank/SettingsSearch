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
import de.KnollFrank.lib.settingssearch.provider.IncludePreferenceInSearchResultsPredicate;
import de.KnollFrank.lib.settingssearch.provider.PrepareShow;
import de.KnollFrank.lib.settingssearch.provider.ShowPreferencePathPredicate;
import de.KnollFrank.lib.settingssearch.results.SearchResultsSorter;
import de.KnollFrank.lib.settingssearch.search.MergedPreferenceScreenDataRepository;
import de.KnollFrank.lib.settingssearch.search.MergedPreferenceScreenDataRepositoryFactory;
import de.KnollFrank.lib.settingssearch.search.MergedPreferenceScreenFactory;
import de.KnollFrank.lib.settingssearch.search.SearchDatabaseDirectoryIO;
import de.KnollFrank.lib.settingssearch.search.SearchPreferenceFragment;
import de.KnollFrank.lib.settingssearch.search.progress.ProgressUpdateListener;
import de.KnollFrank.lib.settingssearch.search.ui.SearchPreferenceFragmentUI;
import de.KnollFrank.lib.settingssearch.search.ui.SearchResultsFragmentUI;

public class SearchPreferenceFragments implements MergedPreferenceScreenDataRepositoryFactory {

    public final SearchConfiguration searchConfiguration;
    private final SearchDatabase searchDatabase;
    private final IncludePreferenceInSearchResultsPredicate includePreferenceInSearchResultsPredicate;
    private final ShowPreferencePathPredicate showPreferencePathPredicate;
    private final PrepareShow prepareShow;
    private final FragmentManager fragmentManager;
    private final Locale locale;
    private final OnUiThreadRunner onUiThreadRunner;
    private final Context context;
    private final Supplier<Optional<AsyncTaskWithProgressUpdateListeners<?>>> createSearchDatabaseTaskSupplier;
    private final SearchPreferenceFragmentUI searchPreferenceFragmentUI;
    private final SearchResultsFragmentUI searchResultsFragmentUI;
    private final SearchResultsSorter searchResultsSorter;

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
                                        final SearchDatabase searchDatabase,
                                        final IncludePreferenceInSearchResultsPredicate includePreferenceInSearchResultsPredicate,
                                        final ShowPreferencePathPredicate showPreferencePathPredicate,
                                        final PrepareShow prepareShow,
                                        final FragmentManager fragmentManager,
                                        final Locale locale,
                                        final OnUiThreadRunner onUiThreadRunner,
                                        final Context context,
                                        final Supplier<Optional<AsyncTaskWithProgressUpdateListeners<?>>> createSearchDatabaseTaskSupplier,
                                        final SearchPreferenceFragmentUI searchPreferenceFragmentUI,
                                        final SearchResultsFragmentUI searchResultsFragmentUI,
                                        final SearchResultsSorter searchResultsSorter) {
        this.searchConfiguration = searchConfiguration;
        this.searchDatabase = searchDatabase;
        this.includePreferenceInSearchResultsPredicate = includePreferenceInSearchResultsPredicate;
        this.showPreferencePathPredicate = showPreferencePathPredicate;
        this.prepareShow = prepareShow;
        this.fragmentManager = fragmentManager;
        this.locale = locale;
        this.onUiThreadRunner = onUiThreadRunner;
        this.context = context;
        this.createSearchDatabaseTaskSupplier = createSearchDatabaseTaskSupplier;
        this.searchPreferenceFragmentUI = searchPreferenceFragmentUI;
        this.searchResultsFragmentUI = searchResultsFragmentUI;
        this.searchResultsSorter = searchResultsSorter;
    }

    public void showSearchPreferenceFragment() {
        showFragment(
                new SearchPreferenceFragment(
                        searchConfiguration,
                        includePreferenceInSearchResultsPredicate,
                        new MergedPreferenceScreenFactory(
                                showPreferencePathPredicate,
                                prepareShow,
                                searchConfiguration.fragmentContainerViewId(),
                                searchDatabase.fragmentFactory(),
                                context,
                                locale,
                                onUiThreadRunner,
                                this,
                                searchResultsFragmentUI,
                                searchResultsSorter),
                        onUiThreadRunner,
                        createSearchDatabaseTaskSupplier,
                        searchPreferenceFragmentUI),
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
                                        searchDatabase.fragmentFactory(),
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
                searchDatabase,
                searchConfiguration.rootPreferenceFragment(),
                progressUpdateListener,
                new SearchDatabaseDirectoryIO(context));
    }
}
