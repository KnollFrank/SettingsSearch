package de.KnollFrank.settingssearch.preference.fragment;

import static de.KnollFrank.lib.settingssearch.graph.GraphConverterFactory.createSearchablePreferenceScreenGraphConverter;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentActivity;

import org.jgrapht.Graph;

import java.util.Locale;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.SearchDatabaseConfig;
import de.KnollFrank.lib.settingssearch.common.Views;
import de.KnollFrank.lib.settingssearch.common.graph.Graphs;
import de.KnollFrank.lib.settingssearch.common.graph.Subtree;
import de.KnollFrank.lib.settingssearch.common.graph.SubtreeReplacer;
import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunner;
import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunnerFactory;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenGraphTransformer;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
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
import de.KnollFrank.settingssearch.SearchDatabaseConfigFactory;

public class SearchDatabaseRootedAtPrefsFragmentFifthAdapter implements SearchablePreferenceScreenGraphTransformer<Configuration> {

    private final @IdRes int FRAGMENT_CONTAINER_VIEW_ID = View.generateViewId();

    @Override
    public SearchablePreferenceScreenGraph transformGraph(final SearchablePreferenceScreenGraph graph,
                                                          final Configuration actualConfiguration,
                                                          final FragmentActivity activityContext) {
        return adaptGraphAtPrefsFragmentFifth(
                graph,
                actualConfiguration,
                SearchDatabaseConfigFactory.createSearchDatabaseConfig(),
                activityContext);
    }

    public SearchablePreferenceScreenGraph adaptGraphAtPrefsFragmentFifth(
            final SearchablePreferenceScreenGraph graph,
            final Configuration newConfiguration,
            final SearchDatabaseConfig searchDatabaseConfig,
            final FragmentActivity activityContext) {
        final OnUiThreadRunner onUiThreadRunner = OnUiThreadRunnerFactory.fromActivity(activityContext);
        onUiThreadRunner.runBlockingOnUiThread(() -> {
            FragmentContainerViewAdder.addInvisibleFragmentContainerViewWithIdToParent(
                    Views.getRootViewContainer(activityContext),
                    FRAGMENT_CONTAINER_VIEW_ID);
            return null;
        });
        final SearchablePreferenceScreen prefsFragmentFifthPreferenceScreen = getPrefsFragmentFifthPreferenceScreen(graph);
        return new SearchablePreferenceScreenGraph(
                SubtreeReplacer.replaceSubtreeWithTree(
                        new Subtree<>(
                                graph.tree(),
                                prefsFragmentFifthPreferenceScreen),
                        getPojoGraphRootedAt(
                                instantiateSearchablePreferenceScreen(
                                        prefsFragmentFifthPreferenceScreen,
                                        createSearchablePreferenceScreenGraphConverter().toJGraphT(graph.tree().graph()),
                                        createGraphPathFactory(searchDatabaseConfig, activityContext),
                                        onUiThreadRunner),
                                graph.locale(),
                                activityContext,
                                searchDatabaseConfig)),
                graph.locale(),
                new ConfigurationBundleConverter().convertForward(newConfiguration));
    }

    private Tree<SearchablePreferenceScreen, SearchablePreference> getPojoGraphRootedAt(
            final PreferenceScreenWithHost root,
            final Locale locale,
            final FragmentActivity activityContext,
            final SearchDatabaseConfig searchDatabaseConfig) {
        return SearchablePreferenceScreenGraphProviderFactory
                .createSearchablePreferenceScreenGraphProvider(
                        FRAGMENT_CONTAINER_VIEW_ID,
                        Views.getRootViewContainer(activityContext),
                        activityContext,
                        activityContext.getSupportFragmentManager(),
                        activityContext,
                        searchDatabaseConfig,
                        locale,
                        (edge, sourceNodeOfEdge, targetNodeOfEdge) -> true)
                .getSearchablePreferenceScreenGraph(root);
    }

    @SuppressWarnings({"UnstableApiUsage"})
    private SearchablePreferenceScreen getPrefsFragmentFifthPreferenceScreen(final SearchablePreferenceScreenGraph graphToSearchIn) {
        return SearchablePreferenceScreens
                .findSearchablePreferenceScreenById(
                        graphToSearchIn.tree().graph().nodes(),
                        "en-de.KnollFrank.settingssearch.preference.fragment.PrefsFragmentFifth Bundle[{some_string_extra=hello world, some_boolean_extra=true}]") // Strings.prefixIdWithLanguage(PrefsFragmentFifth.class.getName(), graphToSearchIn.locale()))
                .orElseThrow();
    }

    private PreferenceScreenWithHost instantiateSearchablePreferenceScreen(
            final SearchablePreferenceScreen searchablePreferenceScreen,
            final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph,
            final GraphPathFactory graphPathFactory,
            final OnUiThreadRunner onUiThreadRunner) {
        final var graphPath = Graphs.getPathFromRootNodeToTarget(graph, searchablePreferenceScreen);
        return onUiThreadRunner
                .runBlockingOnUiThread(() -> graphPathFactory.instantiate(graphPath))
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
