package de.KnollFrank.lib.settingssearch;

import android.content.Context;

import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.db.SearchableInfoAndDialogInfoProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJOEdge;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentInitializer;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactoryAndInitializer;
import de.KnollFrank.lib.settingssearch.fragment.Fragments;
import de.KnollFrank.lib.settingssearch.fragment.PreferenceDialogs;
import de.KnollFrank.lib.settingssearch.fragment.factory.FragmentFactoryAndInitializerWithCache;
import de.KnollFrank.lib.settingssearch.graph.DefaultSearchablePreferenceScreenGraphProvider;
import de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenGraphProvider;
import de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenGraphProviderWrapper;
import de.KnollFrank.lib.settingssearch.provider.IsPreferenceSearchable;
import de.KnollFrank.lib.settingssearch.provider.MergedPreferenceScreenProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceConnected2PreferenceFragmentProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceScreenGraphAvailableListener;
import de.KnollFrank.lib.settingssearch.provider.SearchableDialogInfoOfProvider;
import de.KnollFrank.lib.settingssearch.search.IsPreferenceVisibleAndSearchable;
import de.KnollFrank.lib.settingssearch.search.PreferenceManagerProvider;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoProvider;

// FK-TODO: inline MergedPreferenceScreenProvider into MergedPreferenceScreenFactory?
public class MergedPreferenceScreenFactory {

    private final FragmentManager fragmentManager;
    private final Context context;
    private final IsPreferenceSearchable isPreferenceSearchable;
    private final SearchableInfoProvider searchableInfoProvider;
    private final FragmentFactory fragmentFactory;
    private final Class<? extends PreferenceFragmentCompat> rootPreferenceFragment;
    private final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider;
    private final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener;
    private final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider;
    private final SearchablePreferenceScreenGraphProviderWrapper searchablePreferenceScreenGraphProviderWrapper;

    public MergedPreferenceScreenFactory(
            final FragmentManager fragmentManager,
            final Context context,
            final IsPreferenceSearchable isPreferenceSearchable,
            final SearchableInfoProvider searchableInfoProvider,
            final FragmentFactory fragmentFactory,
            final Class<? extends PreferenceFragmentCompat> rootPreferenceFragment,
            final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider,
            final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener,
            final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider,
            final SearchablePreferenceScreenGraphProviderWrapper searchablePreferenceScreenGraphProviderWrapper) {
        this.fragmentManager = fragmentManager;
        this.context = context;
        this.isPreferenceSearchable = isPreferenceSearchable;
        this.searchableInfoProvider = searchableInfoProvider;
        this.fragmentFactory = fragmentFactory;
        this.rootPreferenceFragment = rootPreferenceFragment;
        this.preferenceDialogAndSearchableInfoProvider = preferenceDialogAndSearchableInfoProvider;
        this.preferenceScreenGraphAvailableListener = preferenceScreenGraphAvailableListener;
        this.preferenceConnected2PreferenceFragmentProvider = preferenceConnected2PreferenceFragmentProvider;
        this.searchablePreferenceScreenGraphProviderWrapper = searchablePreferenceScreenGraphProviderWrapper;
    }

    public MergedPreferenceScreen createMergedPreferenceScreen() {
        final var defaultFragmentInitializer = getDefaultFragmentInitializer();
        final var fragmentFactoryAndInitializer = getFragmentFactoryAndInitializer(defaultFragmentInitializer);
        final var fragments = getFragments(fragmentFactoryAndInitializer);
        return this
                .getMergedPreferenceScreenProvider(
                        fragmentFactoryAndInitializer,
                        PreferenceManagerProvider.getPreferenceManager(
                                fragments,
                                rootPreferenceFragment))
                .getMergedPreferenceScreen(
                        getSearchablePreferenceScreenGraph(
                                defaultFragmentInitializer,
                                fragments,
                                context));
    }

    private DefaultFragmentInitializer getDefaultFragmentInitializer() {
        return new DefaultFragmentInitializer(
                fragmentManager,
                R.id.dummyFragmentContainerView);
    }

    private FragmentFactoryAndInitializer getFragmentFactoryAndInitializer(final DefaultFragmentInitializer defaultFragmentInitializer) {
        return new FragmentFactoryAndInitializer(
                fragmentFactory,
                defaultFragmentInitializer);
    }

    private Fragments getFragments(final FragmentFactoryAndInitializer fragmentFactoryAndInitializer) {
        return new Fragments(
                new FragmentFactoryAndInitializerWithCache(fragmentFactoryAndInitializer),
                context);
    }

    private MergedPreferenceScreenProvider getMergedPreferenceScreenProvider(
            final FragmentFactoryAndInitializer fragmentFactoryAndInitializer,
            final PreferenceManager preferenceManager) {
        return new MergedPreferenceScreenProvider(
                true,
                fragmentFactoryAndInitializer,
                preferenceManager);
    }

    private Graph<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> getSearchablePreferenceScreenGraph(
            final PreferenceDialogs preferenceDialogs,
            final Fragments fragments,
            final Context context) {
        return searchablePreferenceScreenGraphProviderWrapper
                .wrap(
                        getSearchablePreferenceScreenGraphProvider(
                                preferenceDialogs,
                                fragments),
                        context)
                .getSearchablePreferenceScreenGraph();
    }

    private SearchablePreferenceScreenGraphProvider getSearchablePreferenceScreenGraphProvider(
            final PreferenceDialogs preferenceDialogs,
            final Fragments fragments) {
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
}
