package de.KnollFrank.lib.settingssearch.search;

import android.content.Context;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

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
import de.KnollFrank.lib.settingssearch.provider.PreferenceConnected2PreferenceFragmentProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceScreenGraphAvailableListener;
import de.KnollFrank.lib.settingssearch.provider.PreferenceSearchablePredicate;
import de.KnollFrank.lib.settingssearch.provider.PrepareShow;
import de.KnollFrank.lib.settingssearch.provider.ShowPreferencePathPredicate;
import de.KnollFrank.lib.settingssearch.results.SearchResultsDisplayerFactory;
import de.KnollFrank.lib.settingssearch.results.ShowPreferenceScreenAndHighlightPreference;
import de.KnollFrank.lib.settingssearch.results.recyclerview.SearchResultsFragment;
import de.KnollFrank.lib.settingssearch.search.progress.IProgressDisplayer;
import de.KnollFrank.lib.settingssearch.search.provider.IconResourceIdProvider;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoProvider;

public class MergedPreferenceScreenFactory {

    private final ShowPreferencePathPredicate showPreferencePathPredicate;
    private final PrepareShow prepareShow;
    private final @IdRes int fragmentContainerViewId;
    private final Class<? extends PreferenceFragmentCompat> rootPreferenceFragment;
    private final FragmentFactory fragmentFactory;
    private final PreferenceSearchablePredicate preferenceSearchablePredicate;
    private final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider;
    private final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener;
    private final SearchableInfoProvider searchableInfoProvider;
    private final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider;
    private final IconResourceIdProvider iconResourceIdProvider;
    private final Context context;
    private final Locale locale;
    private final OnUiThreadRunner onUiThreadRunner;

    public MergedPreferenceScreenFactory(
            final ShowPreferencePathPredicate showPreferencePathPredicate,
            final PrepareShow prepareShow,
            final @IdRes int fragmentContainerViewId,
            final Class<? extends PreferenceFragmentCompat> rootPreferenceFragment,
            final FragmentFactory fragmentFactory,
            final PreferenceSearchablePredicate preferenceSearchablePredicate,
            final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider,
            final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener,
            final SearchableInfoProvider searchableInfoProvider,
            final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider,
            final IconResourceIdProvider iconResourceIdProvider,
            final Context context,
            final Locale locale,
            final OnUiThreadRunner onUiThreadRunner) {
        this.showPreferencePathPredicate = showPreferencePathPredicate;
        this.prepareShow = prepareShow;
        this.fragmentContainerViewId = fragmentContainerViewId;
        this.rootPreferenceFragment = rootPreferenceFragment;
        this.fragmentFactory = fragmentFactory;
        this.preferenceSearchablePredicate = preferenceSearchablePredicate;
        this.preferenceConnected2PreferenceFragmentProvider = preferenceConnected2PreferenceFragmentProvider;
        this.preferenceScreenGraphAvailableListener = preferenceScreenGraphAvailableListener;
        this.searchableInfoProvider = searchableInfoProvider;
        this.preferenceDialogAndSearchableInfoProvider = preferenceDialogAndSearchableInfoProvider;
        this.iconResourceIdProvider = iconResourceIdProvider;
        this.context = context;
        this.locale = locale;
        this.onUiThreadRunner = onUiThreadRunner;
    }

    public MergedPreferenceScreen getMergedPreferenceScreen(
            final FragmentManager fragmentManager,
            final FragmentManager childFragmentManager,
            final IProgressDisplayer progressDisplayer,
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
                getMergedPreferenceScreenData(
                        progressDisplayer,
                        fragments,
                        preferenceDialogs),
                PreferenceManagerProvider.getPreferenceManager(
                        fragments,
                        rootPreferenceFragment),
                fragmentFactoryAndInitializer);
    }

    public static MergedPreferenceScreen createMergedPreferenceScreen(
            final @IdRes int fragmentContainerViewId,
            final PrepareShow prepareShow,
            final ShowPreferencePathPredicate showPreferencePathPredicate,
            final FragmentManager fragmentManager,
            final MergedPreferenceScreenData mergedPreferenceScreenData,
            final PreferenceManager preferenceManager,
            final FragmentFactoryAndInitializer fragmentFactoryAndInitializer) {
        final PreferencePathNavigator preferencePathNavigator =
                new PreferencePathNavigator(
                        mergedPreferenceScreenData.hostByPreference(),
                        fragmentFactoryAndInitializer,
                        preferenceManager.getContext());
        return new MergedPreferenceScreen(
                mergedPreferenceScreenData.preferencePathByPreference(),
                mergedPreferenceScreenData.preferences(),
                SearchResultsDisplayerFactory.createSearchResultsDisplayer(
                        new SearchResultsFragment(
                                mergedPreferenceScreenData.preferencePathByPreference(),
                                new ShowPreferenceScreenAndHighlightPreference(
                                        preferencePathNavigator,
                                        mergedPreferenceScreenData.preferencePathByPreference(),
                                        fragmentContainerViewId,
                                        prepareShow,
                                        fragmentManager),
                                showPreferencePathPredicate),
                        preferenceManager),
                preferencePathNavigator,
                mergedPreferenceScreenData.hostByPreference());
    }

    private MergedPreferenceScreenData getMergedPreferenceScreenData(
            final IProgressDisplayer progressDisplayer,
            final Fragments fragments,
            final DefaultFragmentInitializer preferenceDialogs) {
        final MergedPreferenceScreenDataRepository mergedPreferenceScreenDataRepository =
                new MergedPreferenceScreenDataRepository(
                        fragments,
                        preferenceDialogs,
                        iconResourceIdProvider,
                        searchableInfoProvider,
                        preferenceDialogAndSearchableInfoProvider,
                        rootPreferenceFragment,
                        preferenceSearchablePredicate,
                        preferenceConnected2PreferenceFragmentProvider,
                        preferenceScreenGraphAvailableListener,
                        progressDisplayer,
                        new SearchDatabaseDirectoryIO(context));
        return mergedPreferenceScreenDataRepository.getMergedPreferenceScreenData(locale);
    }
}
