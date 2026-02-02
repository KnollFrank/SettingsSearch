package de.KnollFrank.lib.settingssearch.graph;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static de.KnollFrank.lib.settingssearch.InstantiateAndInitializeFragmentFactory.createInstantiateAndInitializeFragment;
import static de.KnollFrank.lib.settingssearch.graph.MapFromPojoNodesRemover.removeMapFromPojoNodes;
import static de.KnollFrank.lib.settingssearch.graph.TreeToPojoTreeTransformerTest.PreferenceFragmentWithSinglePreference;
import static de.KnollFrank.settingssearch.preference.fragment.PrefsFragmentFirst.markExtrasOfPreferenceConnectingSrcWithDst;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.test.core.app.ActivityScenario;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.graph.EndpointPair;
import com.google.common.graph.ImmutableValueGraph;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.FragmentClassOfActivity;
import de.KnollFrank.lib.settingssearch.PreferenceScreenOfHostOfActivity;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.PrincipalAndProxyProvider;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.ActivityDescription;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.DefaultPreferenceFragmentIdProvider;
import de.KnollFrank.lib.settingssearch.common.graph.Edge;
import de.KnollFrank.lib.settingssearch.common.graph.Graphs;
import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.common.graph.TreeNode;
import de.KnollFrank.lib.settingssearch.db.SearchableInfoAndDialogInfoProvider;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenToSearchablePreferenceScreenConverter;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceToSearchablePreferenceConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreens;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;
import de.KnollFrank.settingssearch.test.TestActivity;

@RunWith(RobolectricTestRunner.class)
@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
// FK-TODO: wie in SubtreeReplacerTest ASCII-Bäume malen?
public class TreeMergerTest {

    @Test
    public void test_mergeTreeIntoTreeNode_rootNode() {
        final var rootOfGraph = FragmentWithPreferenceCategory.class;
        shouldMergeTreeIntoTreeNode(
                rootOfGraph,
                "de-" + rootOfGraph.getName());
    }

    @Test
    public void test_mergeTreeIntoTreeNode_innerNode() {
        final var rootOfGraph = RootFragmentConnectedToFragmentWithPreferenceCategory.class;
        shouldMergeTreeIntoTreeNode(
                rootOfGraph,
                String.format(
                        "de-%s Bundle[{some preference key: %s -> %s=true}]",
                        FragmentWithPreferenceCategory.class.getName(),
                        rootOfGraph.getName(),
                        FragmentWithPreferenceCategory.class.getName()));
    }

    private static void shouldMergeTreeIntoTreeNode(final Class<? extends PreferenceFragmentCompat> rootOfGraph,
                                                    final String idOfRootOfPartialPojoTree) {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                // Given
                final FragmentClassOfActivity root =
                        new FragmentClassOfActivity(
                                rootOfGraph,
                                new ActivityDescription(
                                        activity.getClass(),
                                        new PersistableBundle()));
                final var pojoTree = transformToPojoTree(createEntityTree(root, List.of(), activity));
                final List<String> preferenceKeys = List.of("key1");
                final SearchablePreferenceScreen mergePointOfTree =
                        SearchablePreferenceScreens
                                .findSearchablePreferenceScreenById(pojoTree.graph().nodes(), idOfRootOfPartialPojoTree)
                                .orElseThrow();
                final var partialEntityTree =
                        createPartialEntityTree(
                                pojoTree,
                                preferenceKeys,
                                mergePointOfTree,
                                activity);

                // When
                final Tree<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> mergedGraph =
                        TreeMerger.mergeTreeIntoTreeNode(
                                transformToPojoTree(partialEntityTree),
                                new TreeNode<>(mergePointOfTree, pojoTree));

                // Then
                final var mergedTreeExpected = transformToPojoTree(createEntityTree(root, preferenceKeys, activity));
                System.out.println(
                        DotGraphDifference.between(
                                GraphConverterFactory
                                        .createSearchablePreferenceScreenGraphConverter()
                                        .toJGraphT(mergedGraph.graph()),
                                GraphConverterFactory
                                        .createSearchablePreferenceScreenGraphConverter()
                                        .toJGraphT(mergedTreeExpected.graph())));
                final GraphDifference graphDifference = GraphDifference.between(mergedGraph.graph(), mergedTreeExpected.graph());
                assertThat(graphDifference.toString(), graphDifference.areEqual(), is(true));
                assertIntegrity(mergedGraph);
            });
        }
    }

    private static void assertIntegrity(final Tree<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> graph) {
        for (final EndpointPair<SearchablePreferenceScreen> edge : graph.graph().edges()) {
            assertPreferenceOfEdgeExistsInSourceScreen(edge, graph);
        }
    }

    private static void assertPreferenceOfEdgeExistsInSourceScreen(final EndpointPair<SearchablePreferenceScreen> edge,
                                                                   final Tree<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> graph) {
        final SearchablePreference searchablePreference = Graphs.getEdgeValue(edge, graph.graph());
        final SearchablePreferenceScreen sourceScreen = edge.source();
        assertThat(
                String.format(
                        "Integrity error: The preference [key=%s] associated with the edge from screen [id=%s] " +
                                "is missing from the source screen's preference hierarchy. " +
                                "This indicates a mismatch between the graph structure and the node content.",
                        searchablePreference.getKey(),
                        sourceScreen.id()),
                preferenceExistsInScreen(searchablePreference, sourceScreen),
                is(true));
    }

    private static boolean preferenceExistsInScreen(final SearchablePreference searchablePreference,
                                                    final SearchablePreferenceScreen screen) {
        return screen
                .allPreferencesOfPreferenceHierarchy()
                .contains(searchablePreference);
    }

    private static Tree<PreferenceScreenOfHostOfActivity, Preference, ImmutableValueGraph<PreferenceScreenOfHostOfActivity, Preference>> createEntityTree(
            final FragmentClassOfActivity root,
            final List<String> preferenceKeys,
            final TestActivity activity) {
        FragmentWithPreferenceCategory.setPreferenceKeys(preferenceKeys);
        final InstantiateAndInitializeFragment instantiateAndInitializeFragment = createInstantiateAndInitializeFragment(activity);
        return PojoTreeTestFactory.createEntityPreferenceScreenTreeRootedAt(
                TreeMergerTest
                        .createPreferenceScreenWithHostProvider(instantiateAndInitializeFragment)
                        .getPreferenceScreenWithHostOfFragment(
                                root,
                                Optional.empty())
                        .orElseThrow(),
                instantiateAndInitializeFragment,
                activity);
    }

    private static Tree<PreferenceScreenOfHostOfActivity, Preference, ImmutableValueGraph<PreferenceScreenOfHostOfActivity, Preference>> createPartialEntityTree(
            final Tree<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> pojoTree,
            final List<String> preferenceKeys,
            final SearchablePreferenceScreen rootOfPartialPojoTree,
            final FragmentActivity activity) {
        FragmentWithPreferenceCategory.setPreferenceKeys(preferenceKeys);
        final InstantiateAndInitializeFragment instantiateAndInitializeFragment = createInstantiateAndInitializeFragment(activity);
        return PojoTreeTestFactory.createEntityPreferenceScreenTreeRootedAt(
                new TreePathInstantiator(createPreferenceScreenWithHostProvider(instantiateAndInitializeFragment))
                        .instantiate(pojoTree.getPathFromRootNodeToTarget(rootOfPartialPojoTree))
                        .endNode(),
                instantiateAndInitializeFragment,
                new AddEdgeToTreePredicate<>() {

                    @Override
                    public boolean shallAddEdgeToTree(final Edge<PreferenceScreenOfHostOfActivity, Preference> edge) {
                        return !"some preference key".equals(edge.value().getKey());
                    }
                },
                activity);
    }

    private static PreferenceScreenWithHostProvider createPreferenceScreenWithHostProvider(
            final InstantiateAndInitializeFragment instantiateAndInitializeFragment) {
        return new PreferenceScreenWithHostProvider(
                instantiateAndInitializeFragment,
                new PrincipalAndProxyProvider(ImmutableBiMap.of()));
    }

    private static Tree<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> transformToPojoTree(
            final Tree<PreferenceScreenOfHostOfActivity, Preference, ImmutableValueGraph<PreferenceScreenOfHostOfActivity, Preference>> entityTree) {
        return removeMapFromPojoNodes(
                createGraphToPojoGraphTransformer().transformTreeToPojoTree(
                        entityTree,
                        Locale.GERMAN));
    }

    private static TreeToPojoTreeTransformer createGraphToPojoGraphTransformer() {
        return new TreeToPojoTreeTransformer(
                new PreferenceScreenToSearchablePreferenceScreenConverter(
                        new PreferenceToSearchablePreferenceConverter(
                                (preference, hostOfPreference) -> Optional.empty(),
                                new SearchableInfoAndDialogInfoProvider(
                                        preference -> Optional.empty(),
                                        (preference, hostOfPreference) -> Optional.empty()))),
                new DefaultPreferenceFragmentIdProvider());
    }

    public static class RootFragmentConnectedToFragmentWithPreferenceCategory extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
            final PreferenceScreen preferenceScreen = getPreferenceManager().createPreferenceScreen(requireContext());
            preferenceScreen.setTitle("root screen title");
            preferenceScreen.setSummary("root screen summary");
            addPreferencesToPreferenceScreen(preferenceScreen);
            setPreferenceScreen(preferenceScreen);
        }

        private void addPreferencesToPreferenceScreen(final PreferenceScreen preferenceScreen) {
            preferenceScreen.addPreference(
                    FragmentWithPreferenceCategory.createPreferenceConnectedToFragment(
                            FragmentWithPreferenceCategory.class,
                            "some preference key",
                            "root preference ",
                            this));
        }
    }

    public static class FragmentWithPreferenceCategory extends PreferenceFragmentCompat {

        private static List<String> preferenceKeys = List.of();

        public static void setPreferenceKeys(final List<String> preferenceKeys) {
            FragmentWithPreferenceCategory.preferenceKeys = preferenceKeys;
        }

        @Override
        public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
            final PreferenceScreen preferenceScreen = getPreferenceManager().createPreferenceScreen(requireContext());
            preferenceScreen.setTitle("screen title");
            preferenceScreen.setSummary("screen summary");
            addPreferencesToPreferenceScreen(preferenceScreen);
            setPreferenceScreen(preferenceScreen);
        }

        private void addPreferencesToPreferenceScreen(final PreferenceScreen preferenceScreen) {
            preferenceScreen.addPreference(createPreference("some preference key"));
            final PreferenceCategory preferenceCategory = createPreferenceCategory();
            preferenceScreen.addPreference(preferenceCategory);
            this
                    .createPreferences(preferenceKeys)
                    .forEach(preferenceCategory::addPreference);
        }

        private PreferenceCategory createPreferenceCategory() {
            final PreferenceCategory preferenceCategory = new PreferenceCategory(requireContext());
            preferenceCategory.setTitle("some category");
            preferenceCategory.setKey("preferenceCategoryKey");
            return preferenceCategory;
        }

        private List<Preference> createPreferences(final List<String> keys) {
            return keys
                    .stream()
                    .map(this::createPreference)
                    .collect(Collectors.toList());
        }

        private Preference createPreference(final String key) {
            return createPreferenceConnectedToFragment(
                    PreferenceFragmentWithSinglePreference.class,
                    key,
                    "preference ",
                    this);
        }

        public static Preference createPreferenceConnectedToFragment(final Class<? extends Fragment> fragment,
                                                                     final String keyOfPreference,
                                                                     final String titlePrefix,
                                                                     final PreferenceFragmentCompat src) {
            final Preference preference = new Preference(src.requireContext());
            preference.setFragment(fragment.getName());
            preference.setTitle(titlePrefix + keyOfPreference + " connected to " + fragment.getSimpleName());
            preference.setKey(keyOfPreference);
            markExtrasOfPreferenceConnectingSrcWithDst(preference, src, fragment);
            return preference;
        }
    }
}
