package de.KnollFrank.lib.settingssearch.graph;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static de.KnollFrank.lib.settingssearch.PreferenceScreensProviderTestHelper.getPreferenceScreenByTitle;
import static de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenGraphProvider1Test.createPreferenceScreenWithHostProvider;
import static de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenGraphProvider1Test.createSearchablePreferenceScreenGraphProviderAndPreferenceScreenWithHostProvider;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.test.core.app.ActivityScenario;

import com.google.common.collect.Iterables;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.GraphWalk;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenGraphProvider1Test.Fragment3ConnectedToFragment4;
import de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenGraphProvider1Test.Fragment4;
import de.KnollFrank.settingssearch.test.TestActivity;

@RunWith(RobolectricTestRunner.class)
public class GraphPathFactoryTest {

    @Test
    public void test_instantiateGraphPath_noNode() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(fragmentActivity -> {
                // Given
                final GraphPathFactory graphPathFactory = new GraphPathFactory(createPreferenceScreenWithHostProvider(fragmentActivity));
                final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> emptyPojoGraph = new DefaultDirectedGraph<>(SearchablePreferenceEdge.class);
                final GraphPath<SearchablePreferenceScreen, SearchablePreferenceEdge> emptyGraphPath = new GraphWalk<>(emptyPojoGraph, List.of(), 0);

                // When
                final GraphPath<PreferenceScreenWithHost, PreferenceEdge> graphPath = graphPathFactory.instantiate(emptyGraphPath);

                // Then
                assertSameSize(graphPath, emptyGraphPath);
            });
        }
    }

    @Test
    public void test_instantiateGraphPath_singleNode() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(fragmentActivity -> {
                // Given
                final GraphPathFactory graphPathFactory = new GraphPathFactory(createPreferenceScreenWithHostProvider(fragmentActivity));
                final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> pojoGraphSingleNode =
                        createSomePojoGraph(
                                fragmentActivity,
                                Fragment4.class);
                final SearchablePreferenceScreen searchablePreferenceScreen = Iterables.getOnlyElement(pojoGraphSingleNode.vertexSet());
                final GraphPath<SearchablePreferenceScreen, SearchablePreferenceEdge> graphPath =
                        new GraphWalk<>(
                                pojoGraphSingleNode,
                                List.of(searchablePreferenceScreen),
                                0);

                // When
                final GraphPath<PreferenceScreenWithHost, PreferenceEdge> graphPathInstantiated = graphPathFactory.instantiate(graphPath);

                // Then
                assertSameSize(graphPathInstantiated, graphPath);
                assertThat(graphPathInstantiated.getEndVertex().host(), is(instanceOf(searchablePreferenceScreen.host())));
            });
        }
    }

    @Test
    public void test_instantiateGraphPath_twoNodes() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(fragmentActivity -> {
                // Given
                final GraphPathFactory graphPathFactory = new GraphPathFactory(createPreferenceScreenWithHostProvider(fragmentActivity));
                final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graphTwoNodes = createSomePojoGraph(fragmentActivity, Fragment3ConnectedToFragment4.class);
                final SearchablePreferenceScreen thirdScreen = getPreferenceScreenByTitle(graphTwoNodes.vertexSet(), "third screen");
                final SearchablePreferenceScreen fourthScreen = getPreferenceScreenByTitle(graphTwoNodes.vertexSet(), "fourth screen");
                final GraphPath<SearchablePreferenceScreen, SearchablePreferenceEdge> graphPath =
                        new GraphWalk<>(
                                graphTwoNodes,
                                List.of(thirdScreen, fourthScreen),
                                0);

                // When
                final GraphPath<PreferenceScreenWithHost, PreferenceEdge> graphPathInstantiated = graphPathFactory.instantiate(graphPath);

                // Then
                assertSameSize(graphPathInstantiated, graphPath);
                assertThat(graphPathInstantiated.getStartVertex().host(), is(instanceOf(thirdScreen.host())));
                assertThat(graphPathInstantiated.getEndVertex().host(), is(instanceOf(fourthScreen.host())));
            });
        }
    }

    private static Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> createSomePojoGraph(
            final FragmentActivity fragmentActivity,
            final Class<? extends Fragment> root) {
        final var result =
                createSearchablePreferenceScreenGraphProviderAndPreferenceScreenWithHostProvider(
                        fragmentActivity,
                        root);
        return result
                .searchablePreferenceScreenGraphProvider()
                .getSearchablePreferenceScreenGraph(
                        result.preferenceScreenWithHost());
    }

    private static void assertSameSize(final GraphPath<?, ?> actual, final GraphPath<?, ?> expected) {
        assertThat(actual.getVertexList().size(), is(expected.getVertexList().size()));
    }
}