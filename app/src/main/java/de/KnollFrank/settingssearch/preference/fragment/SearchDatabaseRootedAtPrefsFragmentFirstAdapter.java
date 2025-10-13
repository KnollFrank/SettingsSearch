package de.KnollFrank.settingssearch.preference.fragment;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceFragmentCompat;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;

import java.util.Locale;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.SearchDatabaseConfig;
import de.KnollFrank.lib.settingssearch.common.Strings;
import de.KnollFrank.lib.settingssearch.common.graph.GraphUtils;
import de.KnollFrank.lib.settingssearch.common.graph.SubtreeReplacer;
import de.KnollFrank.lib.settingssearch.db.preference.db.DAOProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraph;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreens;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactoryAndInitializer;
import de.KnollFrank.lib.settingssearch.fragment.FragmentInitializerFactory;
import de.KnollFrank.lib.settingssearch.fragment.Fragments;
import de.KnollFrank.lib.settingssearch.fragment.factory.FragmentFactoryAndInitializerWithCache;
import de.KnollFrank.lib.settingssearch.fragment.navigation.PreferencePathNavigator;
import de.KnollFrank.lib.settingssearch.fragment.navigation.PreferencePathNavigatorFactory;
import de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenGraphProviderFactory;
import de.KnollFrank.lib.settingssearch.results.recyclerview.FragmentContainerViewAdder;
import de.KnollFrank.settingssearch.SearchDatabaseConfigFactory;

public class SearchDatabaseRootedAtPrefsFragmentFirstAdapter {

    private final @IdRes int FRAGMENT_CONTAINER_VIEW_ID = View.generateViewId();

    private final SubtreeReplacer<SearchablePreferenceScreen, SearchablePreferenceEdge> subtreeReplacer =
            new SubtreeReplacer<>(
                    () -> new DefaultDirectedGraph<>(SearchablePreferenceEdge.class),
                    edge -> new SearchablePreferenceEdge(edge.preference));

    public void adaptSearchDatabaseRootedAtPrefsFragmentFirst(
            final DAOProvider preferencesDatabase,
            final SearchablePreferenceScreenGraph graph,
            final FragmentActivity activityContext) {
        preferencesDatabase
                .searchablePreferenceScreenGraphDAO()
                .persist(getAdaptedGraph(graph, activityContext));
    }

    public SearchablePreferenceScreenGraph getAdaptedGraph(final SearchablePreferenceScreenGraph graph,
                                                           final FragmentActivity activityContext) {
        FragmentContainerViewAdder.addInvisibleFragmentContainerViewWithIdToParent(
                activityContext.findViewById(android.R.id.content),
                FRAGMENT_CONTAINER_VIEW_ID);
        final SearchablePreferenceScreen searchablePreferenceScreen = findSearchablePreferenceScreen(graph);
        final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> newPojoGraph =
                subtreeReplacer.replaceSubtreeWithTree(
                        graph.graph(),
                        searchablePreferenceScreen,
                        getPojoGraphRootedAt(
                                asPreferenceScreenWithHost(
                                        instantiateSearchablePreferenceScreen(
                                                searchablePreferenceScreen,
                                                graph.graph(),
                                                SearchDatabaseConfigFactory.createSearchDatabaseConfig(),
                                                activityContext)),
                                graph.locale(),
                                activityContext));
        return new SearchablePreferenceScreenGraph(
                newPojoGraph,
                graph.locale(),
                graph.processed());
    }

    private Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> getPojoGraphRootedAt(
            final PreferenceScreenWithHost root,
            final Locale locale,
            final FragmentActivity activityContext) {
        return SearchablePreferenceScreenGraphProviderFactory
                .createSearchablePreferenceScreenGraphProvider(
                        FRAGMENT_CONTAINER_VIEW_ID,
                        activityContext.findViewById(android.R.id.content),
                        activityContext,
                        activityContext.getSupportFragmentManager(),
                        activityContext,
                        SearchDatabaseConfigFactory.createSearchDatabaseConfig(),
                        locale)
                .getSearchablePreferenceScreenGraph(root);
    }

    private SearchablePreferenceScreen findSearchablePreferenceScreen(final SearchablePreferenceScreenGraph graphToSearchIn) {
        return SearchablePreferenceScreens
                .findSearchablePreferenceScreenById(
                        graphToSearchIn.graph().vertexSet(),
                        Strings.addLocaleToId(graphToSearchIn.locale(), PrefsFragmentFirst.class.getName()))
                .orElseThrow();
    }

    private PreferenceFragmentCompat instantiateSearchablePreferenceScreen(
            final SearchablePreferenceScreen searchablePreferenceScreen,
            final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph,
            final SearchDatabaseConfig searchDatabaseConfig,
            final FragmentActivity activityContext) {
        final FragmentFactoryAndInitializer fragmentFactoryAndInitializer =
                new FragmentFactoryAndInitializer(
                        searchDatabaseConfig.fragmentFactory,
                        FragmentInitializerFactory.createFragmentInitializer(
                                activityContext,
                                FRAGMENT_CONTAINER_VIEW_ID));
        final Fragments instantiateAndInitializeFragment =
                new Fragments(
                        new FragmentFactoryAndInitializerWithCache(fragmentFactoryAndInitializer),
                        activityContext);
        final PreferencePathNavigator preferencePathNavigator =
                PreferencePathNavigatorFactory.createPreferencePathNavigator(
                        activityContext,
                        fragmentFactoryAndInitializer,
                        instantiateAndInitializeFragment,
                        searchDatabaseConfig.activityInitializerByActivity,
                        searchDatabaseConfig.principalAndProxyProvider);
        // FK-TODO: PreferencePathNavigator should be able to navigate an empty PreferencePath simply by instantiating the root preference fragment.
        return this
                .getPreferencePathLeadingToSearchablePreferenceScreen(
                        searchablePreferenceScreen,
                        graph)
                .map(preferencePath ->
                             (PreferenceFragmentCompat)
                                     preferencePathNavigator
                                             .navigatePreferencePath(preferencePath)
                                             .orElseThrow())
                .orElseGet(() ->
                                   fragmentFactoryAndInitializer.instantiateAndInitializeFragment(
                                           GraphUtils.getRootNode(graph).orElseThrow().host(),
                                           Optional.empty(),
                                           activityContext,
                                           instantiateAndInitializeFragment));
    }

    private Optional<PreferencePath> getPreferencePathLeadingToSearchablePreferenceScreen(
            final SearchablePreferenceScreen searchablePreferenceScreen,
            final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph) {
        return graph
                .incomingEdgesOf(searchablePreferenceScreen)
                .stream()
                .findFirst()
                .map(searchablePreferenceEdge -> searchablePreferenceEdge.preference.getPreferencePath());
    }

    private PreferenceScreenWithHost asPreferenceScreenWithHost(final PreferenceFragmentCompat preferenceFragment) {
        return new PreferenceScreenWithHost(
                preferenceFragment.getPreferenceScreen(),
                preferenceFragment);
    }
}
