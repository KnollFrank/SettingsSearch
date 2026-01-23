package de.KnollFrank.settingssearch.preference.fragment;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentActivity;

import com.google.common.graph.ImmutableValueGraph;

import java.util.Locale;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.SearchDatabaseConfig;
import de.KnollFrank.lib.settingssearch.common.Views;
import de.KnollFrank.lib.settingssearch.common.graph.Subtree;
import de.KnollFrank.lib.settingssearch.common.graph.SubtreeReplacer;
import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunner;
import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunnerFactory;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeTransformer;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenTree;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreens;
import de.KnollFrank.lib.settingssearch.fragment.FragmentInitializerFactory;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragmentFactory;
import de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenTreeProviderFactory;
import de.KnollFrank.lib.settingssearch.graph.TreePathInstantiator;
import de.KnollFrank.lib.settingssearch.results.recyclerview.FragmentContainerViewAdder;
import de.KnollFrank.settingssearch.Configuration;
import de.KnollFrank.settingssearch.SearchDatabaseConfigFactory;

public class SearchDatabaseRootedAtPrefsFragmentFifthAdapter implements SearchablePreferenceScreenTreeTransformer<Configuration> {

    private final @IdRes int FRAGMENT_CONTAINER_VIEW_ID = View.generateViewId();

    @Override
    @SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
    public Tree<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> transformSearchablePreferenceScreenTree(
            final SearchablePreferenceScreenTree<Configuration> searchablePreferenceScreenTree,
            final Configuration targetConfiguration,
            final FragmentActivity activityContext) {
        return adaptGraphAtPrefsFragmentFifth(
                searchablePreferenceScreenTree,
                SearchDatabaseConfigFactory.createSearchDatabaseConfig(),
                activityContext);
    }

    @SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
    public Tree<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> adaptGraphAtPrefsFragmentFifth(
            final SearchablePreferenceScreenTree<Configuration> searchablePreferenceScreenTree,
            final SearchDatabaseConfig searchDatabaseConfig,
            final FragmentActivity activityContext) {
        final OnUiThreadRunner onUiThreadRunner = OnUiThreadRunnerFactory.fromActivity(activityContext);
        onUiThreadRunner.runBlockingOnUiThread(() -> {
            FragmentContainerViewAdder.addInvisibleFragmentContainerViewWithIdToParent(
                    Views.getRootViewContainer(activityContext),
                    FRAGMENT_CONTAINER_VIEW_ID);
            return null;
        });
        final SearchablePreferenceScreen prefsFragmentFifthPreferenceScreen = getPrefsFragmentFifthPreferenceScreen(searchablePreferenceScreenTree);
        return SubtreeReplacer.replaceSubtreeWithTree(
                new Subtree<>(
                        searchablePreferenceScreenTree.tree(),
                        prefsFragmentFifthPreferenceScreen),
                getPojoGraphRootedAt(
                        instantiateSearchablePreferenceScreen(
                                prefsFragmentFifthPreferenceScreen,
                                searchablePreferenceScreenTree.tree(),
                                createTreePathInstantiator(searchDatabaseConfig, activityContext),
                                onUiThreadRunner),
                        searchablePreferenceScreenTree.locale(),
                        activityContext,
                        searchDatabaseConfig));
    }

    @SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
    private Tree<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> getPojoGraphRootedAt(
            final PreferenceScreenWithHost root,
            final Locale locale,
            final FragmentActivity activityContext,
            final SearchDatabaseConfig searchDatabaseConfig) {
        return SearchablePreferenceScreenTreeProviderFactory
                .createSearchablePreferenceScreenTreeProvider(
                        FRAGMENT_CONTAINER_VIEW_ID,
                        Views.getRootViewContainer(activityContext),
                        activityContext,
                        activityContext.getSupportFragmentManager(),
                        activityContext,
                        searchDatabaseConfig,
                        locale,
                        edge -> true)
                .getSearchablePreferenceScreenTree(root);
    }

    private SearchablePreferenceScreen getPrefsFragmentFifthPreferenceScreen(final SearchablePreferenceScreenTree<Configuration> treeToSearchIn) {
        return SearchablePreferenceScreens
                .findSearchablePreferenceScreenById(
                        treeToSearchIn.tree().graph().nodes(),
                        "en-de.KnollFrank.settingssearch.preference.fragment.PrefsFragmentFifth Bundle[{some_string_extra=hello world, some_boolean_extra=true}]") // Strings.prefixIdWithLanguage(PrefsFragmentFifth.class.getName(), graphToSearchIn.locale()))
                .orElseThrow();
    }

    private PreferenceScreenWithHost instantiateSearchablePreferenceScreen(
            final SearchablePreferenceScreen searchablePreferenceScreen,
            @SuppressWarnings({"UnstableApiUsage", "NullableProblems"}) final Tree<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> tree,
            final TreePathInstantiator treePathInstantiator,
            final OnUiThreadRunner onUiThreadRunner) {
        final var treePath = tree.getPathFromRootNodeToTarget(searchablePreferenceScreen);
        return onUiThreadRunner
                .runBlockingOnUiThread(() -> treePathInstantiator.instantiate(treePath))
                .endNode();
    }

    private TreePathInstantiator createTreePathInstantiator(final SearchDatabaseConfig searchDatabaseConfig,
                                                            final FragmentActivity activityContext) {
        return new TreePathInstantiator(
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
