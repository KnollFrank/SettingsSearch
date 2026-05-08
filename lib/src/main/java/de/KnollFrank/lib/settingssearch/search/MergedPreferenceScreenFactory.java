package de.KnollFrank.lib.settingssearch.search;

import android.os.PersistableBundle;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.util.Locale;

import de.KnollFrank.lib.settingssearch.MergedPreferenceScreen;
import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunner;
import de.KnollFrank.lib.settingssearch.db.preference.db.PreferencesDatabase;
import de.KnollFrank.lib.settingssearch.db.preference.db.SearchablePreferenceScreenTreeRepository;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactoryAndInitializer;
import de.KnollFrank.lib.settingssearch.fragment.FragmentInitializerFactory;
import de.KnollFrank.lib.settingssearch.fragment.Fragments;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;
import de.KnollFrank.lib.settingssearch.fragment.PreferenceDialogsFactory;
import de.KnollFrank.lib.settingssearch.fragment.factory.FragmentFactoryAndInitializerRegistry;
import de.KnollFrank.lib.settingssearch.fragment.navigation.PreferencePathNavigator;
import de.KnollFrank.lib.settingssearch.provider.PreferenceSearchablePredicate;
import de.KnollFrank.lib.settingssearch.provider.PrepareShow;
import de.KnollFrank.lib.settingssearch.provider.ShowPreferencePathPredicate;
import de.KnollFrank.lib.settingssearch.results.MarkupsFactory;
import de.KnollFrank.lib.settingssearch.results.NavigatePreferencePathAndHighlightPreference;
import de.KnollFrank.lib.settingssearch.results.SearchResultsDisplayer;
import de.KnollFrank.lib.settingssearch.results.SearchResultsSorter;
import de.KnollFrank.lib.settingssearch.results.ShowSettingsFragmentAndHighlightSetting;
import de.KnollFrank.lib.settingssearch.results.recyclerview.PreferencePathDisplayer;
import de.KnollFrank.lib.settingssearch.results.recyclerview.SearchResultsFragment;
import de.KnollFrank.lib.settingssearch.search.progress.ProgressUpdateListener;
import de.KnollFrank.lib.settingssearch.search.ui.SearchResultsFragmentUI;

public class MergedPreferenceScreenFactory<C> {

    private final ShowPreferencePathPredicate showPreferencePathPredicate;
    private final PreferencePathNavigator preferencePathNavigator;
    private final PrepareShow prepareShow;
    private final FragmentFactory fragmentFactory;
    private final MarkupsFactory markupsFactory;
    private final FragmentActivity activity;
    private final Locale locale;
    private final OnUiThreadRunner onUiThreadRunner;
    private final MergedPreferenceScreenDataRepositoryProvider<C> mergedPreferenceScreenDataRepositoryProvider;
    private final SearchResultsFragmentUI searchResultsFragmentUI;
    private final SearchResultsSorter searchResultsSorter;
    private final PreferencePathDisplayer preferencePathDisplayer;
    private final ShowSettingsFragmentAndHighlightSetting showSettingsFragmentAndHighlightSetting;
    private final PreferencesDatabase<C> preferencesDatabase;
    private final PreferenceSearchablePredicate preferenceSearchablePredicate;

    public MergedPreferenceScreenFactory(
            final ShowPreferencePathPredicate showPreferencePathPredicate,
            final PreferencePathNavigator preferencePathNavigator,
            final PrepareShow prepareShow,
            final FragmentFactory fragmentFactory,
            final MarkupsFactory markupsFactory,
            final FragmentActivity activity,
            final Locale locale,
            final OnUiThreadRunner onUiThreadRunner,
            final MergedPreferenceScreenDataRepositoryProvider<C> mergedPreferenceScreenDataRepositoryProvider,
            final SearchResultsFragmentUI searchResultsFragmentUI,
            final SearchResultsSorter searchResultsSorter,
            final PreferencePathDisplayer preferencePathDisplayer,
            final ShowSettingsFragmentAndHighlightSetting showSettingsFragmentAndHighlightSetting,
            final PreferencesDatabase<C> preferencesDatabase,
            final PreferenceSearchablePredicate preferenceSearchablePredicate) {
        this.showPreferencePathPredicate = showPreferencePathPredicate;
        this.preferencePathNavigator = preferencePathNavigator;
        this.prepareShow = prepareShow;
        this.fragmentFactory = fragmentFactory;
        this.markupsFactory = markupsFactory;
        this.activity = activity;
        this.locale = locale;
        this.onUiThreadRunner = onUiThreadRunner;
        this.mergedPreferenceScreenDataRepositoryProvider = mergedPreferenceScreenDataRepositoryProvider;
        this.searchResultsFragmentUI = searchResultsFragmentUI;
        this.searchResultsSorter = searchResultsSorter;
        this.preferencePathDisplayer = preferencePathDisplayer;
        this.showSettingsFragmentAndHighlightSetting = showSettingsFragmentAndHighlightSetting;
        this.preferencesDatabase = preferencesDatabase;
        this.preferenceSearchablePredicate = preferenceSearchablePredicate;
    }

    public MergedPreferenceScreen<C> getMergedPreferenceScreen(
            final FragmentManager childFragmentManager,
            final ProgressUpdateListener progressUpdateListener,
            final @IdRes int containerViewId,
            final PersistableBundle configuration) {
        final InstantiateAndInitializeFragment instantiateAndInitializeFragment =
                new Fragments(
                        new FragmentFactoryAndInitializerRegistry(
                                new FragmentFactoryAndInitializer(
                                        fragmentFactory,
                                        FragmentInitializerFactory.createFragmentInitializer(
                                                childFragmentManager,
                                                containerViewId,
                                                onUiThreadRunner,
                                                preferenceSearchablePredicate))),
                        activity);
        mergedPreferenceScreenDataRepositoryProvider
                .createMergedPreferenceScreenDataRepository(
                        PreferenceDialogsFactory.createPreferenceDialogs(
                                childFragmentManager,
                                containerViewId,
                                onUiThreadRunner,
                                preferenceSearchablePredicate),
                        activity,
                        preferencesDatabase,
                        progressUpdateListener,
                        instantiateAndInitializeFragment)
                .fillSearchDatabaseWithPreferences(locale, configuration);
        return createMergedPreferenceScreen(
                preferencePathNavigator,
                prepareShow,
                showPreferencePathPredicate,
                preferencePathDisplayer,
                preferencesDatabase.searchablePreferenceScreenTreeRepository(),
                searchResultsFragmentUI,
                markupsFactory,
                searchResultsSorter,
                showSettingsFragmentAndHighlightSetting,
                activity);
    }

    public static <C> MergedPreferenceScreen<C> createMergedPreferenceScreen(
            final PreferencePathNavigator preferencePathNavigator,
            final PrepareShow prepareShow,
            final ShowPreferencePathPredicate showPreferencePathPredicate,
            final PreferencePathDisplayer preferencePathDisplayer,
            final SearchablePreferenceScreenTreeRepository<C> treeRepository,
            final SearchResultsFragmentUI searchResultsFragmentUI,
            final MarkupsFactory markupsFactory,
            final SearchResultsSorter searchResultsSorter,
            final ShowSettingsFragmentAndHighlightSetting showSettingsFragmentAndHighlightSetting,
            final FragmentActivity activity) {
        return new MergedPreferenceScreen<>(
                treeRepository,
                new SearchResultsDisplayer(
                        new SearchResultsFragment(
                                new NavigatePreferencePathAndHighlightPreference(
                                        preferencePathNavigator,
                                        prepareShow,
                                        showSettingsFragmentAndHighlightSetting,
                                        activity),
                                showPreferencePathPredicate,
                                preferencePathDisplayer,
                                searchResultsFragmentUI),
                        markupsFactory,
                        searchResultsSorter));
    }
}
