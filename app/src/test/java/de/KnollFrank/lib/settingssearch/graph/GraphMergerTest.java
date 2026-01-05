package de.KnollFrank.lib.settingssearch.graph;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static de.KnollFrank.lib.settingssearch.InstantiateAndInitializeFragmentFactory.createInstantiateAndInitializeFragment;
import static de.KnollFrank.lib.settingssearch.graph.GraphToPojoGraphTransformerTest.PreferenceFragmentWithSinglePreference;
import static de.KnollFrank.lib.settingssearch.graph.MapFromPojoNodesRemover.removeMapFromPojoNodes;
import static de.KnollFrank.settingssearch.preference.fragment.PrefsFragmentFirst.markExtrasOfPreferenceConnectingSrcWithDst;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.test.core.app.ActivityScenario;

import com.google.common.collect.ImmutableBiMap;

import org.jgrapht.Graph;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.PrincipalAndProxyProvider;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.DefaultPreferenceFragmentIdProvider;
import de.KnollFrank.lib.settingssearch.common.graph.GraphAtNode;
import de.KnollFrank.lib.settingssearch.common.graph.Graphs;
import de.KnollFrank.lib.settingssearch.db.SearchableInfoAndDialogInfoProvider;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenToSearchablePreferenceScreenConverter;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceToSearchablePreferenceConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreens;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;
import de.KnollFrank.settingssearch.test.TestActivity;

@RunWith(RobolectricTestRunner.class)
public class GraphMergerTest {

    @Test
    public void test_mergeSrcGraphIntoDstGraphAtMergePoint_rootNode() {
        final var rootOfGraph = FragmentWithPreferenceCategory.class;
        shouldMergeSrcGraphIntoDstGraphAtMergePoint(
                rootOfGraph,
                "de-" + rootOfGraph.getName());
    }

    @Test
    public void test_mergeSrcGraphIntoDstGraphAtMergePoint_innerNode() {
        final var rootOfGraph = RootFragmentConnectedToFragmentWithPreferenceCategory.class;
        shouldMergeSrcGraphIntoDstGraphAtMergePoint(
                rootOfGraph,
                String.format(
                        "de-%s Bundle[{some preference key: %s -> %s=true}]",
                        FragmentWithPreferenceCategory.class.getName(),
                        rootOfGraph.getName(),
                        FragmentWithPreferenceCategory.class.getName()));
    }

    private static void shouldMergeSrcGraphIntoDstGraphAtMergePoint(final Class<? extends PreferenceFragmentCompat> rootOfGraph,
                                                                    final String idOfRootOfPartialPojoGraph) {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                // Given
                final var pojoGraph = transformToPojoGraph(createEntityGraph(rootOfGraph, List.of(), activity));
                final List<String> preferenceKeys = List.of("key1");
                final SearchablePreferenceScreen mergePointOfGraph =
                        SearchablePreferenceScreens
                                .findSearchablePreferenceScreenById(pojoGraph.vertexSet(), idOfRootOfPartialPojoGraph)
                                .orElseThrow();
                final var partialEntityGraph =
                        createPartialEntityGraph(
                                pojoGraph,
                                preferenceKeys,
                                mergePointOfGraph,
                                activity);

                // When
                final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> mergedGraph =
                        GraphMerger.mergeSrcGraphIntoDstGraphAtMergePoint(
                                transformToPojoGraph(partialEntityGraph),
                                new GraphAtNode<>(pojoGraph, mergePointOfGraph));

                // Then
                final var mergedGraphExpected = transformToPojoGraph(createEntityGraph(rootOfGraph, preferenceKeys, activity));
                System.out.println(DotGraphDifference.between(mergedGraph, mergedGraphExpected));
                final GraphDifference graphDifference = GraphDifference.between(mergedGraph, mergedGraphExpected);
                assertThat(graphDifference.toString(), graphDifference.areEqual(), is(true));
                assertIntegrity(mergedGraph);
            });
        }
    }

    private static void assertIntegrity(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph) {
        for (final SearchablePreferenceEdge edge : graph.edgeSet()) {
            assertPreferenceOfEdgeExistsInSourceScreen(edge, graph);
        }
    }

    private static void assertPreferenceOfEdgeExistsInSourceScreen(final SearchablePreferenceEdge edge,
                                                                   final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph) {
        final SearchablePreferenceScreen sourceScreen = graph.getEdgeSource(edge);
        assertThat(
                String.format(
                        "Integrity error: The preference [key=%s] associated with the edge from screen [id=%s] " +
                                "is missing from the source screen's preference hierarchy. " +
                                "This indicates a mismatch between the graph structure and the node content.",
                        edge.preference.getKey(),
                        sourceScreen.id()),
                preferenceOfEdgeExistsInScreen(edge, sourceScreen),
                is(true));
    }

    private static boolean preferenceOfEdgeExistsInScreen(final SearchablePreferenceEdge edge,
                                                          final SearchablePreferenceScreen screen) {
        return screen
                .allPreferencesOfPreferenceHierarchy()
                .contains(edge.preference);
    }

    private static Graph<PreferenceScreenWithHost, PreferenceEdge> createEntityGraph(
            final Class<? extends PreferenceFragmentCompat> root,
            final List<String> preferenceKeys,
            final TestActivity activity) {
        FragmentWithPreferenceCategory.setPreferenceKeys(preferenceKeys);
        final InstantiateAndInitializeFragment instantiateAndInitializeFragment = createInstantiateAndInitializeFragment(activity);
        return PojoGraphTestFactory.createEntityPreferenceScreenGraphRootedAt(
                GraphMergerTest
                        .createPreferenceScreenWithHostProvider(instantiateAndInitializeFragment)
                        .getPreferenceScreenWithHostOfFragment(
                                root,
                                Optional.empty())
                        .orElseThrow(),
                instantiateAndInitializeFragment,
                activity);
    }

    private static Graph<PreferenceScreenWithHost, PreferenceEdge> createPartialEntityGraph(
            final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> pojoGraph,
            final List<String> preferenceKeys,
            final SearchablePreferenceScreen rootOfPartialPojoGraph,
            final FragmentActivity activity) {
        FragmentWithPreferenceCategory.setPreferenceKeys(preferenceKeys);
        final InstantiateAndInitializeFragment instantiateAndInitializeFragment = createInstantiateAndInitializeFragment(activity);
        return PojoGraphTestFactory.createEntityPreferenceScreenGraphRootedAt(
                new GraphPathFactory(createPreferenceScreenWithHostProvider(instantiateAndInitializeFragment))
                        .instantiate(
                                Graphs.getPathFromRootNodeToTarget(
                                        pojoGraph,
                                        rootOfPartialPojoGraph))
                        .getEndVertex(),
                instantiateAndInitializeFragment,
                new AddEdgeToGraphPredicate() {

                    @Override
                    public boolean shallAddEdgeToGraph(final PreferenceEdge edge,
                                                       final PreferenceScreenWithHost sourceNodeOfEdge,
                                                       final PreferenceScreenWithHost targetNodeOfEdge) {
                        return !"some preference key".equals(edge.preference.getKey());
                    }
                },
                activity);
    }

    public static PreferenceScreenWithHostProvider createPreferenceScreenWithHostProvider(final InstantiateAndInitializeFragment instantiateAndInitializeFragment) {
        return new PreferenceScreenWithHostProvider(
                instantiateAndInitializeFragment,
                new PrincipalAndProxyProvider(ImmutableBiMap.of()));
    }

    private static Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> transformToPojoGraph(final Graph<PreferenceScreenWithHost, PreferenceEdge> entityGraph) {
        return removeMapFromPojoNodes(
                createGraphToPojoGraphTransformer().transformGraphToPojoGraph(
                        entityGraph,
                        Locale.GERMAN));
    }

    private static GraphToPojoGraphTransformer createGraphToPojoGraphTransformer() {
        return new GraphToPojoGraphTransformer(
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
