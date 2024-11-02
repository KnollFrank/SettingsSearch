package de.KnollFrank.lib.settingssearch.search;

import static de.KnollFrank.lib.settingssearch.fragment.Fragments.showFragment;

import android.content.Context;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;

import org.jgrapht.Graph;

import java.util.function.Consumer;

import de.KnollFrank.lib.settingssearch.MergedPreferenceScreen;
import de.KnollFrank.lib.settingssearch.MergedPreferenceScreenFactory;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.R;
import de.KnollFrank.lib.settingssearch.SearchablePreferenceScreenProvider;
import de.KnollFrank.lib.settingssearch.client.SearchConfiguration;
import de.KnollFrank.lib.settingssearch.common.Keyboard;
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
import de.KnollFrank.lib.settingssearch.fragment.factory.FragmentFactoryAndInitializerWithCache;
import de.KnollFrank.lib.settingssearch.graph.DefaultSearchablePreferenceScreenGraphProvider;
import de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenGraphProvider;
import de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenGraphProviderWrapper;
import de.KnollFrank.lib.settingssearch.provider.IsPreferenceSearchable;
import de.KnollFrank.lib.settingssearch.provider.PreferenceConnected2PreferenceFragmentProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceScreenGraphAvailableListener;
import de.KnollFrank.lib.settingssearch.provider.PrepareShow;
import de.KnollFrank.lib.settingssearch.provider.SearchableDialogInfoOfProvider;
import de.KnollFrank.lib.settingssearch.provider.ShowPreferencePathPredicate;
import de.KnollFrank.lib.settingssearch.results.SearchResultsPreferenceFragment;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoProvider;

public class SearchPreferenceFragment extends Fragment {

    private final IsPreferenceSearchable isPreferenceSearchable;
    private final SearchableInfoProvider searchableInfoProvider;
    private final ShowPreferencePathPredicate showPreferencePathPredicate;
    private final FragmentFactory fragmentFactory;
    private final SearchConfiguration searchConfiguration;
    private final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider;
    private final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener;
    private final PrepareShow prepareShow;
    private final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider;
    private final SearchablePreferenceScreenGraphProviderWrapper searchablePreferenceScreenGraphProviderWrapper;

    public SearchPreferenceFragment(final SearchConfiguration searchConfiguration,
                                    final IsPreferenceSearchable isPreferenceSearchable,
                                    final SearchableInfoProvider searchableInfoProvider,
                                    final ShowPreferencePathPredicate showPreferencePathPredicate,
                                    final FragmentFactory fragmentFactory,
                                    final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider,
                                    final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener,
                                    final PrepareShow prepareShow,
                                    final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider,
                                    final SearchablePreferenceScreenGraphProviderWrapper searchablePreferenceScreenGraphProviderWrapper) {
        super(R.layout.searchpreference_fragment);
        this.searchConfiguration = searchConfiguration;
        this.isPreferenceSearchable = isPreferenceSearchable;
        this.searchableInfoProvider = searchableInfoProvider;
        this.showPreferencePathPredicate = showPreferencePathPredicate;
        this.fragmentFactory = fragmentFactory;
        this.preferenceDialogAndSearchableInfoProvider = preferenceDialogAndSearchableInfoProvider;
        this.preferenceScreenGraphAvailableListener = preferenceScreenGraphAvailableListener;
        this.prepareShow = prepareShow;
        this.preferenceConnected2PreferenceFragmentProvider = preferenceConnected2PreferenceFragmentProvider;
        this.searchablePreferenceScreenGraphProviderWrapper = searchablePreferenceScreenGraphProviderWrapper;
    }

    @Override
    public void onResume() {
        super.onResume();
        final MergedPreferenceScreen mergedPreferenceScreen = getMergedPreferenceScreen();
        showSearchResultsPreferenceFragment(
                mergedPreferenceScreen,
                searchResultsPreferenceFragment -> configureSearchView(mergedPreferenceScreen));
    }

    private MergedPreferenceScreen getMergedPreferenceScreen() {
        final DefaultFragmentInitializer preferenceDialogs =
                new DefaultFragmentInitializer(
                        getChildFragmentManager(),
                        R.id.dummyFragmentContainerView);
        final FragmentFactoryAndInitializer fragmentFactoryAndInitializer =
                new FragmentFactoryAndInitializer(fragmentFactory, preferenceDialogs);
        final Fragments fragments =
                new Fragments(
                        new FragmentFactoryAndInitializerWithCache(fragmentFactoryAndInitializer),
                        requireActivity());
        return MergedPreferenceScreenFactory.getMergedPreferenceScreen(
                getMergedPreferenceScreenData(
                        searchablePreferenceScreenGraphProviderWrapper,
                        new DefaultSearchablePreferenceScreenGraphProvider(
                                searchConfiguration.rootPreferenceFragment().getName(),
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
                                                preferenceDialogAndSearchableInfoProvider))),
                        requireActivity()),
                PreferenceManagerProvider.getPreferenceManager(
                        fragments,
                        searchConfiguration.rootPreferenceFragment()),
                fragmentFactoryAndInitializer);
    }

    private static MergedPreferenceScreenData getMergedPreferenceScreenData(
            final SearchablePreferenceScreenGraphProviderWrapper searchablePreferenceScreenGraphProviderWrapper,
            final SearchablePreferenceScreenGraphProvider searchablePreferenceScreenGraphProvider,
            final Context context) {
        final boolean persist = true;
        if (persist) {
            final MergedPreferenceScreenData mergedPreferenceScreenData =
                    MergedPreferenceScreenDataFactory.getMergedPreferenceScreenData(
                            getSearchablePreferenceScreenGraph(
                                    searchablePreferenceScreenGraphProviderWrapper,
                                    searchablePreferenceScreenGraphProvider,
                                    context));
            // MergedPreferenceScreenDataDAO.persist(mergedPreferenceScreenData, null, null, null);
            return mergedPreferenceScreenData;
        } else {
            return MergedPreferenceScreenDataDAO.load(null, null, null);
        }
    }

    private static Graph<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> getSearchablePreferenceScreenGraph(
            final SearchablePreferenceScreenGraphProviderWrapper wrapper,
            final SearchablePreferenceScreenGraphProvider searchablePreferenceScreenGraphProvider,
            final Context context) {
        return wrapper
                .wrap(
                        searchablePreferenceScreenGraphProvider,
                        context)
                .getSearchablePreferenceScreenGraph();
    }

    private void showSearchResultsPreferenceFragment(final MergedPreferenceScreen mergedPreferenceScreen,
                                                     final Consumer<SearchResultsPreferenceFragment> onFragmentStarted) {
        showFragment(
                new SearchResultsPreferenceFragment(
                        mergedPreferenceScreen.searchResultsDisplayer(),
                        mergedPreferenceScreen.preferencePathNavigator(),
                        searchConfiguration.fragmentContainerViewId(),
                        showPreferencePathPredicate,
                        prepareShow),
                onFragmentStarted,
                false,
                R.id.searchResultsFragmentContainerView,
                getChildFragmentManager());
    }

    private void configureSearchView(final MergedPreferenceScreen mergedPreferenceScreen) {
        final SearchView searchView = requireView().findViewById(R.id.searchView);
        SearchViewConfigurer.configureSearchView(
                searchView,
                searchConfiguration.queryHint(),
                new SearchAndDisplay(
                        new PreferenceSearcher(mergedPreferenceScreen.allPreferencesForSearch()),
                        mergedPreferenceScreen.searchResultsDisplayer()));
        selectSearchView(searchView);
        searchView.setQuery(searchView.getQuery(), true);
    }

    private void selectSearchView(final SearchView searchView) {
        searchView.requestFocus();
        Keyboard.showKeyboard(getActivity(), searchView);
    }
}
