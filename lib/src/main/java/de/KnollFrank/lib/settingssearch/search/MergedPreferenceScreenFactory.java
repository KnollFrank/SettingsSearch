package de.KnollFrank.lib.settingssearch.search;

import static de.KnollFrank.lib.settingssearch.search.ComputeAndPersistMergedPreferenceScreenData.computeAndPersistMergedPreferenceScreenData;
import static de.KnollFrank.lib.settingssearch.search.ComputeAndPersistMergedPreferenceScreenData.getFileInputStream;
import static de.KnollFrank.lib.settingssearch.search.ComputeAndPersistMergedPreferenceScreenData.getFileName;

import android.content.Context;
import android.content.res.Resources;

import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceFragmentCompat;

import org.jgrapht.Graph;

import java.util.function.Supplier;

import de.KnollFrank.lib.settingssearch.MergedPreferenceScreen;
import de.KnollFrank.lib.settingssearch.MergedPreferenceScreens;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.R;
import de.KnollFrank.lib.settingssearch.SearchablePreferenceScreenProvider;
import de.KnollFrank.lib.settingssearch.db.SearchableInfoAndDialogInfoProvider;
import de.KnollFrank.lib.settingssearch.db.preference.dao.MergedPreferenceScreenDataDAO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.MergedPreferenceScreenData;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJOEdge;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentInitializer;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactoryAndInitializer;
import de.KnollFrank.lib.settingssearch.fragment.Fragments;
import de.KnollFrank.lib.settingssearch.fragment.PreferenceDialogs;
import de.KnollFrank.lib.settingssearch.fragment.factory.FragmentFactoryAndInitializerWithCache;
import de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenGraphProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceConnected2PreferenceFragmentProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceScreenGraphAvailableListener;
import de.KnollFrank.lib.settingssearch.provider.PreferenceSearchablePredicate;
import de.KnollFrank.lib.settingssearch.provider.SearchableDialogInfoOfProvider;
import de.KnollFrank.lib.settingssearch.search.provider.IconResourceIdProvider;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoProvider;

public class MergedPreferenceScreenFactory {

    private final Class<? extends PreferenceFragmentCompat> rootPreferenceFragment;
    private final FragmentFactory fragmentFactory;
    private final PreferenceSearchablePredicate preferenceSearchablePredicate;
    private final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider;
    private final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener;
    private final SearchableInfoProvider searchableInfoProvider;
    private final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider;
    private final IconResourceIdProvider iconResourceIdProvider;
    private final MergedPreferenceScreenDataInput mergedPreferenceScreenDataInput;
    private final MergedPreferenceScreenDataMode mergedPreferenceScreenDataMode;
    private final Resources resources;

    public MergedPreferenceScreenFactory(
            final Class<? extends PreferenceFragmentCompat> rootPreferenceFragment,
            final FragmentFactory fragmentFactory,
            final PreferenceSearchablePredicate preferenceSearchablePredicate,
            final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider,
            final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener,
            final SearchableInfoProvider searchableInfoProvider,
            final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider,
            final IconResourceIdProvider iconResourceIdProvider,
            final MergedPreferenceScreenDataInput mergedPreferenceScreenDataInput,
            final MergedPreferenceScreenDataMode mergedPreferenceScreenDataMode,
            final Resources resources) {
        this.rootPreferenceFragment = rootPreferenceFragment;
        this.fragmentFactory = fragmentFactory;
        this.preferenceSearchablePredicate = preferenceSearchablePredicate;
        this.preferenceConnected2PreferenceFragmentProvider = preferenceConnected2PreferenceFragmentProvider;
        this.preferenceScreenGraphAvailableListener = preferenceScreenGraphAvailableListener;
        this.searchableInfoProvider = searchableInfoProvider;
        this.preferenceDialogAndSearchableInfoProvider = preferenceDialogAndSearchableInfoProvider;
        this.iconResourceIdProvider = iconResourceIdProvider;
        this.mergedPreferenceScreenDataInput = mergedPreferenceScreenDataInput;
        this.mergedPreferenceScreenDataMode = mergedPreferenceScreenDataMode;
        this.resources = resources;
    }

    public MergedPreferenceScreen getMergedPreferenceScreen(final FragmentManager childFragmentManager,
                                                            final Context context) {
        final DefaultFragmentInitializer preferenceDialogs =
                new DefaultFragmentInitializer(
                        childFragmentManager,
                        R.id.dummyFragmentContainerView);
        final FragmentFactoryAndInitializer fragmentFactoryAndInitializer =
                new FragmentFactoryAndInitializer(fragmentFactory, preferenceDialogs);
        final Fragments fragments =
                new Fragments(
                        new FragmentFactoryAndInitializerWithCache(fragmentFactoryAndInitializer),
                        context);
        return MergedPreferenceScreens.createMergedPreferenceScreen(
                getMergedPreferenceScreenData(
                        () -> getSearchablePreferenceScreenGraphProvider(fragments, preferenceDialogs).getSearchablePreferenceScreenGraph(),
                        mergedPreferenceScreenDataInput,
                        context,
                        mergedPreferenceScreenDataMode,
                        resources),
                PreferenceManagerProvider.getPreferenceManager(
                        fragments,
                        rootPreferenceFragment),
                fragmentFactoryAndInitializer);
    }

    private SearchablePreferenceScreenGraphProvider getSearchablePreferenceScreenGraphProvider(
            final Fragments fragments,
            final PreferenceDialogs preferenceDialogs) {
        return new SearchablePreferenceScreenGraphProvider(
                rootPreferenceFragment.getName(),
                new PreferenceScreenWithHostProvider(
                        fragments,
                        new SearchablePreferenceScreenProvider(
                                new PreferenceVisibleAndSearchablePredicate(
                                        preferenceSearchablePredicate))),
                preferenceConnected2PreferenceFragmentProvider,
                preferenceScreenGraphAvailableListener,
                new SearchableInfoAndDialogInfoProvider(
                        searchableInfoProvider,
                        new SearchableDialogInfoOfProvider(
                                preferenceDialogs,
                                preferenceDialogAndSearchableInfoProvider)),
                new IconProvider(iconResourceIdProvider));
    }

    // FK-TODO: remove resources when context is available, and use context.getResources()? Dito multiple places in this library
    private static MergedPreferenceScreenData getMergedPreferenceScreenData(
            final Supplier<Graph<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge>> searchablePreferenceScreenGraphSupplier,
            final MergedPreferenceScreenDataInput mergedPreferenceScreenDataInput,
            final Context context,
            final MergedPreferenceScreenDataMode mergedPreferenceScreenDataMode,
            final Resources resources) {
        return switch (mergedPreferenceScreenDataMode) {
            case PERSIST -> computeAndPersistMergedPreferenceScreenData(
                    searchablePreferenceScreenGraphSupplier,
                    mergedPreferenceScreenDataInput,
                    context,
                    resources);
            case LOAD -> loadMergedPreferenceScreenData(mergedPreferenceScreenDataInput, context);
        };
    }

    private static MergedPreferenceScreenData loadMergedPreferenceScreenData(final MergedPreferenceScreenDataInput mergedPreferenceScreenDataInput, final Context context) {
        return MergedPreferenceScreenDataDAO.load(
                getFileInputStream(getFileName(mergedPreferenceScreenDataInput.preferences(), context.getResources()), context),
                getFileInputStream(getFileName(mergedPreferenceScreenDataInput.preferencePathByPreference(), context.getResources()), context),
                getFileInputStream(getFileName(mergedPreferenceScreenDataInput.hostByPreference(), context.getResources()), context));
    }
}
