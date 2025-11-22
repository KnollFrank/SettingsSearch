package de.KnollFrank.settingssearch.preference.fragment;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentActivity;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;

import java.util.Locale;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.SearchDatabaseConfig;
import de.KnollFrank.lib.settingssearch.common.graph.GraphUtils;
import de.KnollFrank.lib.settingssearch.common.graph.SubtreeReplacer;
import de.KnollFrank.lib.settingssearch.db.preference.db.DAOProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraph;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreens;
import de.KnollFrank.lib.settingssearch.fragment.FragmentInitializerFactory;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragmentFactory;
import de.KnollFrank.lib.settingssearch.graph.GraphPathFactory;
import de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenGraphProviderFactory;
import de.KnollFrank.lib.settingssearch.results.recyclerview.FragmentContainerViewAdder;
import de.KnollFrank.settingssearch.Configuration;
import de.KnollFrank.settingssearch.ConfigurationBundleConverter;

public class SearchDatabaseRootedAtPrefsFragmentFifthAdapter {

    private final @IdRes int FRAGMENT_CONTAINER_VIEW_ID = View.generateViewId();

    private final SubtreeReplacer<SearchablePreferenceScreen, SearchablePreferenceEdge> subtreeReplacer =
            new SubtreeReplacer<>(
                    () -> new DefaultDirectedGraph<>(SearchablePreferenceEdge.class),
                    edge -> new SearchablePreferenceEdge(edge.preference));

    public void adaptSearchDatabaseRootedAtPrefsFragmentFifth(
            final DAOProvider preferencesDatabase,
            final SearchablePreferenceScreenGraph graph,
            final Configuration newConfiguration,
            final SearchDatabaseConfig searchDatabaseConfig,
            final FragmentActivity activityContext) {
        preferencesDatabase
                .searchablePreferenceScreenGraphDAO()
                .persist(getAdaptedGraph(graph, newConfiguration, searchDatabaseConfig, activityContext));
    }

    public SearchablePreferenceScreenGraph getAdaptedGraph(final SearchablePreferenceScreenGraph graph,
                                                           final Configuration newConfiguration,
                                                           final SearchDatabaseConfig searchDatabaseConfig,
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
                                instantiateSearchablePreferenceScreen(
                                        searchablePreferenceScreen,
                                        graph.graph(),
                                        createGraphPathFactory(searchDatabaseConfig, activityContext)),
                                graph.locale(),
                                activityContext,
                                searchDatabaseConfig));
        return new SearchablePreferenceScreenGraph(
                newPojoGraph,
                graph.locale(),
                new ConfigurationBundleConverter().doForward(newConfiguration));
    }

    private Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> getPojoGraphRootedAt(
            final PreferenceScreenWithHost root,
            final Locale locale,
            final FragmentActivity activityContext,
            final SearchDatabaseConfig searchDatabaseConfig) {
        return SearchablePreferenceScreenGraphProviderFactory
                .createSearchablePreferenceScreenGraphProvider(
                        FRAGMENT_CONTAINER_VIEW_ID,
                        activityContext.findViewById(android.R.id.content),
                        activityContext,
                        activityContext.getSupportFragmentManager(),
                        activityContext,
                        searchDatabaseConfig,
                        locale)
                .getSearchablePreferenceScreenGraph(root);
    }

    private SearchablePreferenceScreen findSearchablePreferenceScreen(final SearchablePreferenceScreenGraph graphToSearchIn) {
        return SearchablePreferenceScreens
                .findSearchablePreferenceScreenById(
                        graphToSearchIn.graph().vertexSet(),
                        "en-de.KnollFrank.settingssearch.preference.fragment.PrefsFragmentFifth Bundle[{some_string_extra=hello world, some_boolean_extra=true}]") // Strings.prefixIdWithLanguage(PrefsFragmentFifth.class.getName(), graphToSearchIn.locale()))
                .orElseThrow();
    }

    private PreferenceScreenWithHost instantiateSearchablePreferenceScreen(
            final SearchablePreferenceScreen searchablePreferenceScreen,
            final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph,
            final GraphPathFactory graphPathFactory) {
        return graphPathFactory
                .instantiate(GraphUtils.getPathFromRootNodeToSink(graph, searchablePreferenceScreen))
                .getEndVertex();
    }

    private GraphPathFactory createGraphPathFactory(final SearchDatabaseConfig searchDatabaseConfig,
                                                    final FragmentActivity activityContext) {
        return new GraphPathFactory(
                new PreferenceScreenWithHostProvider(
                        InstantiateAndInitializeFragmentFactory.createInstantiateAndInitializeFragment(
                                searchDatabaseConfig.fragmentFactory,
                                FragmentInitializerFactory.createFragmentInitializer(
                                        activityContext,
                                        FRAGMENT_CONTAINER_VIEW_ID,
                                        searchDatabaseConfig.preferenceSearchablePredicate),
                                activityContext),
                        searchDatabaseConfig.principalAndProxyProvider));
    }
}
