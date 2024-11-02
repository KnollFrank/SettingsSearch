package de.KnollFrank.lib.settingssearch.search;

import android.content.Context;

import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceFragmentCompat;

import org.jgrapht.Graph;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.function.Supplier;

import de.KnollFrank.lib.settingssearch.MergedPreferenceScreen;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.R;
import de.KnollFrank.lib.settingssearch.SearchablePreferenceScreenProvider;
import de.KnollFrank.lib.settingssearch.db.SearchableInfoAndDialogInfoProvider;
import de.KnollFrank.lib.settingssearch.db.preference.dao.MergedPreferenceScreenDataDAO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.MergedPreferenceScreenData;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.MergedPreferenceScreenDataFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJOEdge;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentInitializer;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactoryAndInitializer;
import de.KnollFrank.lib.settingssearch.fragment.Fragments;
import de.KnollFrank.lib.settingssearch.fragment.PreferenceDialogs;
import de.KnollFrank.lib.settingssearch.fragment.factory.FragmentFactoryAndInitializerWithCache;
import de.KnollFrank.lib.settingssearch.graph.DefaultSearchablePreferenceScreenGraphProvider;
import de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenGraphProviderWrapper;
import de.KnollFrank.lib.settingssearch.provider.IsPreferenceSearchable;
import de.KnollFrank.lib.settingssearch.provider.PreferenceConnected2PreferenceFragmentProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceScreenGraphAvailableListener;
import de.KnollFrank.lib.settingssearch.provider.SearchableDialogInfoOfProvider;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoProvider;

public class MergedPreferenceScreenFactory {

    private final Class<? extends PreferenceFragmentCompat> rootPreferenceFragment;
    private final FragmentFactory fragmentFactory;
    private final SearchablePreferenceScreenGraphProviderWrapper searchablePreferenceScreenGraphProviderWrapper;
    private final IsPreferenceSearchable isPreferenceSearchable;
    private final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider;
    private final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener;
    private final SearchableInfoProvider searchableInfoProvider;
    private final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider;
    private final MergedPreferenceScreenDataInput mergedPreferenceScreenDataInput;

    public MergedPreferenceScreenFactory(
            final Class<? extends PreferenceFragmentCompat> rootPreferenceFragment,
            final FragmentFactory fragmentFactory,
            final SearchablePreferenceScreenGraphProviderWrapper searchablePreferenceScreenGraphProviderWrapper,
            final IsPreferenceSearchable isPreferenceSearchable,
            final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider,
            final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener,
            final SearchableInfoProvider searchableInfoProvider,
            final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider,
            final MergedPreferenceScreenDataInput mergedPreferenceScreenDataInput) {
        this.rootPreferenceFragment = rootPreferenceFragment;
        this.fragmentFactory = fragmentFactory;
        this.searchablePreferenceScreenGraphProviderWrapper = searchablePreferenceScreenGraphProviderWrapper;
        this.isPreferenceSearchable = isPreferenceSearchable;
        this.preferenceConnected2PreferenceFragmentProvider = preferenceConnected2PreferenceFragmentProvider;
        this.preferenceScreenGraphAvailableListener = preferenceScreenGraphAvailableListener;
        this.searchableInfoProvider = searchableInfoProvider;
        this.preferenceDialogAndSearchableInfoProvider = preferenceDialogAndSearchableInfoProvider;
        this.mergedPreferenceScreenDataInput = mergedPreferenceScreenDataInput;
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
        return MergedPreferenceScreen.of(
                getMergedPreferenceScreenData(
                        () -> getSearchablePreferenceScreenGraph(fragments, preferenceDialogs, context),
                        mergedPreferenceScreenDataInput,
                        context),
                PreferenceManagerProvider.getPreferenceManager(
                        fragments,
                        rootPreferenceFragment),
                fragmentFactoryAndInitializer);
    }

    private Graph<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> getSearchablePreferenceScreenGraph(
            final Fragments fragments,
            final PreferenceDialogs preferenceDialogs,
            final Context context) {
        return searchablePreferenceScreenGraphProviderWrapper
                .wrap(
                        getSearchablePreferenceScreenGraphProvider(fragments, preferenceDialogs),
                        context)
                .getSearchablePreferenceScreenGraph();
    }

    private DefaultSearchablePreferenceScreenGraphProvider getSearchablePreferenceScreenGraphProvider(
            final Fragments fragments,
            final PreferenceDialogs preferenceDialogs) {
        return new DefaultSearchablePreferenceScreenGraphProvider(
                rootPreferenceFragment.getName(),
                new PreferenceScreenWithHostProvider(
                        fragments,
                        new SearchablePreferenceScreenProvider(
                                new IsPreferenceVisibleAndSearchable(
                                        isPreferenceSearchable))),
                preferenceConnected2PreferenceFragmentProvider,
                preferenceScreenGraphAvailableListener,
                new SearchableInfoAndDialogInfoProvider(
                        searchableInfoProvider,
                        new SearchableDialogInfoOfProvider(
                                preferenceDialogs,
                                preferenceDialogAndSearchableInfoProvider)));
    }

    private static MergedPreferenceScreenData getMergedPreferenceScreenData(
            final Supplier<Graph<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge>> searchablePreferenceScreenGraphSupplier,
            final MergedPreferenceScreenDataInput mergedPreferenceScreenDataInput,
            final Context context) {
        // FK-TODO: make persist a param
        final boolean persist = false;
        if (persist) {
            final MergedPreferenceScreenData mergedPreferenceScreenData =
                    MergedPreferenceScreenDataFactory.getMergedPreferenceScreenData(
                            searchablePreferenceScreenGraphSupplier.get());
            MergedPreferenceScreenDataDAO.persist(
                    mergedPreferenceScreenData,
                    getFileOutputStream("all_preferences_for_search.json", context),
                    getFileOutputStream("preference_path_by_preference.json", context),
                    getFileOutputStream("host_by_preference.json", context));
            return mergedPreferenceScreenData;
        } else {
            return MergedPreferenceScreenDataDAO.load(
                    context.getResources().openRawResource(mergedPreferenceScreenDataInput.allPreferencesForSearch()),
                    context.getResources().openRawResource(mergedPreferenceScreenDataInput.preferencePathByPreference()),
                    context.getResources().openRawResource(mergedPreferenceScreenDataInput.hostByPreference()));
        }
    }

    private static FileOutputStream getFileOutputStream(final String fileName, final Context context) {
        try {
            return context.openFileOutput(fileName, Context.MODE_PRIVATE);
        } catch (final FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
