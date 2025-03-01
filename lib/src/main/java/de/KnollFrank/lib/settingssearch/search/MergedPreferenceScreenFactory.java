package de.KnollFrank.lib.settingssearch.search;

import static de.KnollFrank.lib.settingssearch.fragment.navigation.PreferencePathNavigatorFactory.createPreferencePathNavigator;

import android.app.Activity;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.MergedPreferenceScreen;
import de.KnollFrank.lib.settingssearch.PrincipalProvider;
import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunner;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentInitializer;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactoryAndInitializer;
import de.KnollFrank.lib.settingssearch.fragment.Fragments;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;
import de.KnollFrank.lib.settingssearch.fragment.factory.FragmentFactoryAndInitializerWithCache;
import de.KnollFrank.lib.settingssearch.provider.ActivityInitializer;
import de.KnollFrank.lib.settingssearch.provider.PrepareShow;
import de.KnollFrank.lib.settingssearch.provider.ShowPreferencePathPredicate;
import de.KnollFrank.lib.settingssearch.results.MarkupsFactory;
import de.KnollFrank.lib.settingssearch.results.NavigatePreferencePathAndHighlightPreference;
import de.KnollFrank.lib.settingssearch.results.SearchResultsDisplayer;
import de.KnollFrank.lib.settingssearch.results.SearchResultsFilter;
import de.KnollFrank.lib.settingssearch.results.SearchResultsSorter;
import de.KnollFrank.lib.settingssearch.results.ShowSettingsFragmentAndHighlightSetting;
import de.KnollFrank.lib.settingssearch.results.recyclerview.PreferencePathDisplayer;
import de.KnollFrank.lib.settingssearch.results.recyclerview.SearchResultsFragment;
import de.KnollFrank.lib.settingssearch.search.progress.ProgressUpdateListener;
import de.KnollFrank.lib.settingssearch.search.ui.SearchResultsFragmentUI;

public class MergedPreferenceScreenFactory {

    private final ShowPreferencePathPredicate showPreferencePathPredicate;
    private final PrepareShow prepareShow;
    private final FragmentFactory fragmentFactory;
    private final MarkupsFactory markupsFactory;
    private final FragmentActivity activity;
    private final Locale locale;
    private final OnUiThreadRunner onUiThreadRunner;
    private final MergedPreferenceScreenDataRepositoryFactory mergedPreferenceScreenDataRepositoryFactory;
    private final SearchResultsFragmentUI searchResultsFragmentUI;
    private final SearchResultsFilter searchResultsFilter;
    private final SearchResultsSorter searchResultsSorter;
    private final PreferencePathDisplayer preferencePathDisplayer;
    private final Map<Class<? extends Activity>, ActivityInitializer<?>> activityInitializerByActivity;
    private final PrincipalProvider principalProvider;
    private final ShowSettingsFragmentAndHighlightSetting showSettingsFragmentAndHighlightSetting;

    public MergedPreferenceScreenFactory(
            final ShowPreferencePathPredicate showPreferencePathPredicate,
            final PrepareShow prepareShow,
            final FragmentFactory fragmentFactory,
            final MarkupsFactory markupsFactory,
            final FragmentActivity activity,
            final Locale locale,
            final OnUiThreadRunner onUiThreadRunner,
            final MergedPreferenceScreenDataRepositoryFactory mergedPreferenceScreenDataRepositoryFactory,
            final SearchResultsFragmentUI searchResultsFragmentUI,
            final SearchResultsFilter searchResultsFilter,
            final SearchResultsSorter searchResultsSorter,
            final PreferencePathDisplayer preferencePathDisplayer,
            final Map<Class<? extends Activity>, ActivityInitializer<?>> activityInitializerByActivity,
            final PrincipalProvider principalProvider,
            final ShowSettingsFragmentAndHighlightSetting showSettingsFragmentAndHighlightSetting) {
        this.showPreferencePathPredicate = showPreferencePathPredicate;
        this.prepareShow = prepareShow;
        this.fragmentFactory = fragmentFactory;
        this.markupsFactory = markupsFactory;
        this.activity = activity;
        this.locale = locale;
        this.onUiThreadRunner = onUiThreadRunner;
        this.mergedPreferenceScreenDataRepositoryFactory = mergedPreferenceScreenDataRepositoryFactory;
        this.searchResultsFragmentUI = searchResultsFragmentUI;
        this.searchResultsFilter = searchResultsFilter;
        this.searchResultsSorter = searchResultsSorter;
        this.preferencePathDisplayer = preferencePathDisplayer;
        this.activityInitializerByActivity = activityInitializerByActivity;
        this.principalProvider = principalProvider;
        this.showSettingsFragmentAndHighlightSetting = showSettingsFragmentAndHighlightSetting;
    }

    public MergedPreferenceScreen getMergedPreferenceScreen(
            final FragmentManager childFragmentManager,
            final ProgressUpdateListener progressUpdateListener,
            final @IdRes int containerViewId) {
        final DefaultFragmentInitializer preferenceDialogs =
                new DefaultFragmentInitializer(
                        childFragmentManager,
                        containerViewId,
                        onUiThreadRunner);
        final FragmentFactoryAndInitializer fragmentFactoryAndInitializer =
                new FragmentFactoryAndInitializer(fragmentFactory, preferenceDialogs);
        final InstantiateAndInitializeFragment instantiateAndInitializeFragment =
                new Fragments(
                        new FragmentFactoryAndInitializerWithCache(fragmentFactoryAndInitializer),
                        activity);
        return createMergedPreferenceScreen(
                prepareShow,
                showPreferencePathPredicate,
                preferencePathDisplayer,
                mergedPreferenceScreenDataRepositoryFactory
                        .createMergedPreferenceScreenDataRepository(
                                preferenceDialogs,
                                activity,
                                progressUpdateListener,
                                instantiateAndInitializeFragment)
                        .persistOrLoadPreferences(locale),
                fragmentFactoryAndInitializer,
                searchResultsFragmentUI,
                markupsFactory,
                searchResultsFilter,
                searchResultsSorter,
                instantiateAndInitializeFragment,
                activityInitializerByActivity,
                principalProvider,
                showSettingsFragmentAndHighlightSetting,
                activity);
    }

    public static MergedPreferenceScreen createMergedPreferenceScreen(
            final PrepareShow prepareShow,
            final ShowPreferencePathPredicate showPreferencePathPredicate,
            final PreferencePathDisplayer preferencePathDisplayer,
            final Set<SearchablePreference> preferences,
            final FragmentFactoryAndInitializer fragmentFactoryAndInitializer,
            final SearchResultsFragmentUI searchResultsFragmentUI,
            final MarkupsFactory markupsFactory,
            final SearchResultsFilter searchResultsFilter,
            final SearchResultsSorter searchResultsSorter,
            final InstantiateAndInitializeFragment instantiateAndInitializeFragment,
            final Map<Class<? extends Activity>, ActivityInitializer<?>> activityInitializerByActivity,
            final PrincipalProvider principalProvider,
            final ShowSettingsFragmentAndHighlightSetting showSettingsFragmentAndHighlightSetting,
            final FragmentActivity activity) {
        return new MergedPreferenceScreen(
                preferences,
                new SearchResultsDisplayer(
                        new SearchResultsFragment(
                                new NavigatePreferencePathAndHighlightPreference(
                                        createPreferencePathNavigator(
                                                activity,
                                                fragmentFactoryAndInitializer,
                                                instantiateAndInitializeFragment,
                                                activityInitializerByActivity,
                                                principalProvider),
                                        prepareShow,
                                        showSettingsFragmentAndHighlightSetting,
                                        activity),
                                showPreferencePathPredicate,
                                preferencePathDisplayer,
                                searchResultsFragmentUI),
                        markupsFactory,
                        searchResultsFilter,
                        searchResultsSorter));
    }
}
