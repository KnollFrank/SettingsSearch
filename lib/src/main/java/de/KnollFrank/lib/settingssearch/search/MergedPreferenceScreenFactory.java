package de.KnollFrank.lib.settingssearch.search;

import static de.KnollFrank.lib.settingssearch.common.IOUtils.getFileInputStream;
import static de.KnollFrank.lib.settingssearch.search.ComputeAndPersistMergedPreferenceScreenData.computeAndPersistMergedPreferenceScreenData;

import android.content.Context;

import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceFragmentCompat;

import org.jgrapht.Graph;

import java.io.File;
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

    public MergedPreferenceScreenFactory(
            final Class<? extends PreferenceFragmentCompat> rootPreferenceFragment,
            final FragmentFactory fragmentFactory,
            final PreferenceSearchablePredicate preferenceSearchablePredicate,
            final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider,
            final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener,
            final SearchableInfoProvider searchableInfoProvider,
            final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider,
            final IconResourceIdProvider iconResourceIdProvider) {
        this.rootPreferenceFragment = rootPreferenceFragment;
        this.fragmentFactory = fragmentFactory;
        this.preferenceSearchablePredicate = preferenceSearchablePredicate;
        this.preferenceConnected2PreferenceFragmentProvider = preferenceConnected2PreferenceFragmentProvider;
        this.preferenceScreenGraphAvailableListener = preferenceScreenGraphAvailableListener;
        this.searchableInfoProvider = searchableInfoProvider;
        this.preferenceDialogAndSearchableInfoProvider = preferenceDialogAndSearchableInfoProvider;
        this.iconResourceIdProvider = iconResourceIdProvider;
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
                        context),
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

    private static MergedPreferenceScreenData getMergedPreferenceScreenData(
            final Supplier<Graph<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge>> searchablePreferenceScreenGraphSupplier,
            final Context context) {
        final MergedPreferenceScreenDataInput mergedPreferenceScreenDataInput = getMergedPreferenceScreenDataInput(context);
        if (!mergedPreferenceScreenDataInput.preferences().exists()) {
            computeAndPersistMergedPreferenceScreenData(searchablePreferenceScreenGraphSupplier, mergedPreferenceScreenDataInput);
        }
        return loadMergedPreferenceScreenData(mergedPreferenceScreenDataInput);
    }

    private static MergedPreferenceScreenDataInput getMergedPreferenceScreenDataInput(final Context context) {
        final File directory = context.getDir("settingssearch", Context.MODE_PRIVATE);
        return new MergedPreferenceScreenDataInput(
                new File(directory, "preferences.json"),
                new File(directory, "preference_path_by_preference.json"),
                new File(directory, "host_by_preference.json"));
    }

    private static MergedPreferenceScreenData loadMergedPreferenceScreenData(
            final MergedPreferenceScreenDataInput mergedPreferenceScreenDataInput) {
        return MergedPreferenceScreenDataDAO.load(
                getFileInputStream(mergedPreferenceScreenDataInput.preferences()),
                getFileInputStream(mergedPreferenceScreenDataInput.preferencePathByPreference()),
                getFileInputStream(mergedPreferenceScreenDataInput.hostByPreference()));
    }
}
