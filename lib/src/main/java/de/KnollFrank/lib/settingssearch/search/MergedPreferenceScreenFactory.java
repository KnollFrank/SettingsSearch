package de.KnollFrank.lib.settingssearch.search;

import android.content.Context;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Locale;

import de.KnollFrank.lib.settingssearch.MergedPreferenceScreen;
import de.KnollFrank.lib.settingssearch.MergedPreferenceScreens;
import de.KnollFrank.lib.settingssearch.R;
import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunner;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.MergedPreferenceScreenData;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentInitializer;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactoryAndInitializer;
import de.KnollFrank.lib.settingssearch.fragment.Fragments;
import de.KnollFrank.lib.settingssearch.fragment.factory.FragmentFactoryAndInitializerWithCache;
import de.KnollFrank.lib.settingssearch.graph.PreferenceScreenGraphListener;
import de.KnollFrank.lib.settingssearch.provider.PreferenceConnected2PreferenceFragmentProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceScreenGraphAvailableListener;
import de.KnollFrank.lib.settingssearch.provider.PreferenceSearchablePredicate;
import de.KnollFrank.lib.settingssearch.provider.PrepareShow;
import de.KnollFrank.lib.settingssearch.provider.ShowPreferencePathPredicate;
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
            final Locale locale) {
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
    }

    public MergedPreferenceScreen getMergedPreferenceScreen(
            final FragmentManager fragmentManager,
            final FragmentManager childFragmentManager,
            final OnUiThreadRunner onUiThreadRunner,
            final IProgressDisplayer progressDisplayer,
            final PreferenceScreenGraphListener preferenceScreenGraphListener) {
        final DefaultFragmentInitializer preferenceDialogs =
                new DefaultFragmentInitializer(
                        childFragmentManager,
                        R.id.dummyFragmentContainerView,
                        onUiThreadRunner);
        final FragmentFactoryAndInitializer fragmentFactoryAndInitializer =
                new FragmentFactoryAndInitializer(fragmentFactory, preferenceDialogs);
        final Fragments fragments =
                new Fragments(
                        new FragmentFactoryAndInitializerWithCache(fragmentFactoryAndInitializer),
                        context);
        return MergedPreferenceScreens.createMergedPreferenceScreen(
                fragmentContainerViewId,
                prepareShow,
                showPreferencePathPredicate,
                fragmentManager,
                getMergedPreferenceScreenData(
                        progressDisplayer,
                        preferenceScreenGraphListener,
                        fragments,
                        preferenceDialogs),
                PreferenceManagerProvider.getPreferenceManager(
                        fragments,
                        rootPreferenceFragment),
                fragmentFactoryAndInitializer);
    }

    private MergedPreferenceScreenData getMergedPreferenceScreenData(
            final IProgressDisplayer progressDisplayer,
            final PreferenceScreenGraphListener preferenceScreenGraphListener,
            final Fragments fragments,
            final DefaultFragmentInitializer preferenceDialogs) {
        final MergedPreferenceScreenDataRepository mergedPreferenceScreenDataRepository =
                new MergedPreferenceScreenDataRepository(
                        fragments,
                        preferenceDialogs,
                        preferenceScreenGraphListener,
                        iconResourceIdProvider,
                        searchableInfoProvider,
                        preferenceDialogAndSearchableInfoProvider,
                        rootPreferenceFragment,
                        preferenceSearchablePredicate,
                        preferenceConnected2PreferenceFragmentProvider,
                        preferenceScreenGraphAvailableListener,
                        locale,
                        context,
                        progressDisplayer);
        return mergedPreferenceScreenDataRepository.getMergedPreferenceScreenData();
    }
}
