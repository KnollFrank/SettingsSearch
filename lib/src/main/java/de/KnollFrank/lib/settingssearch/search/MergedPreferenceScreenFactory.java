package de.KnollFrank.lib.settingssearch.search;

import android.content.Context;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentManager;

import java.util.Locale;

import de.KnollFrank.lib.settingssearch.MergedPreferenceScreen;
import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunner;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.MergedPreferenceScreenData;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentInitializer;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactoryAndInitializer;
import de.KnollFrank.lib.settingssearch.fragment.Fragments;
import de.KnollFrank.lib.settingssearch.fragment.PreferencePathNavigator;
import de.KnollFrank.lib.settingssearch.fragment.factory.FragmentFactoryAndInitializerWithCache;
import de.KnollFrank.lib.settingssearch.provider.PrepareShow;
import de.KnollFrank.lib.settingssearch.provider.ShowPreferencePathPredicate;
import de.KnollFrank.lib.settingssearch.results.SearchResultsDisplayerFactory;
import de.KnollFrank.lib.settingssearch.results.SearchResultsSorter;
import de.KnollFrank.lib.settingssearch.results.ShowPreferenceScreenAndHighlightPreference;
import de.KnollFrank.lib.settingssearch.results.recyclerview.SearchResultsFragment;
import de.KnollFrank.lib.settingssearch.search.progress.ProgressUpdateListener;
import de.KnollFrank.lib.settingssearch.search.ui.SearchResultsFragmentUI;

public class MergedPreferenceScreenFactory {

    private final ShowPreferencePathPredicate showPreferencePathPredicate;
    private final PrepareShow prepareShow;
    private final @IdRes int fragmentContainerViewId;
    private final FragmentFactory fragmentFactory;
    private final Context context;
    private final Locale locale;
    private final OnUiThreadRunner onUiThreadRunner;
    private final MergedPreferenceScreenDataRepositoryFactory mergedPreferenceScreenDataRepositoryFactory;
    private final SearchResultsFragmentUI searchResultsFragmentUI;
    private final SearchResultsSorter searchResultsSorter;

    public MergedPreferenceScreenFactory(
            final ShowPreferencePathPredicate showPreferencePathPredicate,
            final PrepareShow prepareShow,
            final @IdRes int fragmentContainerViewId,
            final FragmentFactory fragmentFactory,
            final Context context,
            final Locale locale,
            final OnUiThreadRunner onUiThreadRunner,
            final MergedPreferenceScreenDataRepositoryFactory mergedPreferenceScreenDataRepositoryFactory,
            final SearchResultsFragmentUI searchResultsFragmentUI,
            final SearchResultsSorter searchResultsSorter) {
        this.showPreferencePathPredicate = showPreferencePathPredicate;
        this.prepareShow = prepareShow;
        this.fragmentContainerViewId = fragmentContainerViewId;
        this.fragmentFactory = fragmentFactory;
        this.context = context;
        this.locale = locale;
        this.onUiThreadRunner = onUiThreadRunner;
        this.mergedPreferenceScreenDataRepositoryFactory = mergedPreferenceScreenDataRepositoryFactory;
        this.searchResultsFragmentUI = searchResultsFragmentUI;
        this.searchResultsSorter = searchResultsSorter;
    }

    public MergedPreferenceScreen getMergedPreferenceScreen(
            final FragmentManager fragmentManager,
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
        final Fragments fragments =
                new Fragments(
                        new FragmentFactoryAndInitializerWithCache(fragmentFactoryAndInitializer),
                        context);
        return createMergedPreferenceScreen(
                fragmentContainerViewId,
                prepareShow,
                showPreferencePathPredicate,
                fragmentManager,
                mergedPreferenceScreenDataRepositoryFactory
                        .createMergedPreferenceScreenDataRepository(
                                preferenceDialogs,
                                context,
                                progressUpdateListener,
                                fragments)
                        .persistOrLoadMergedPreferenceScreenData(locale),
                fragmentFactoryAndInitializer,
                searchResultsFragmentUI,
                context,
                searchResultsSorter);
    }

    public static MergedPreferenceScreen createMergedPreferenceScreen(
            final @IdRes int fragmentContainerViewId,
            final PrepareShow prepareShow,
            final ShowPreferencePathPredicate showPreferencePathPredicate,
            final FragmentManager fragmentManager,
            final MergedPreferenceScreenData mergedPreferenceScreenData,
            final FragmentFactoryAndInitializer fragmentFactoryAndInitializer,
            final SearchResultsFragmentUI searchResultsFragmentUI,
            final Context context,
            final SearchResultsSorter searchResultsSorter) {
        final PreferencePathNavigator preferencePathNavigator =
                new PreferencePathNavigator(
                        fragmentFactoryAndInitializer,
                        context);
        return new MergedPreferenceScreen(
                mergedPreferenceScreenData.preferences(),
                SearchResultsDisplayerFactory.createSearchResultsDisplayer(
                        new SearchResultsFragment(
                                new ShowPreferenceScreenAndHighlightPreference(
                                        preferencePathNavigator,
                                        fragmentContainerViewId,
                                        prepareShow,
                                        fragmentManager),
                                showPreferencePathPredicate,
                                searchResultsFragmentUI),
                        context,
                        searchResultsSorter),
                preferencePathNavigator,
                mergedPreferenceScreenData.hostByPreference());
    }
}
