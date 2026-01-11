package de.KnollFrank.lib.settingssearch.graph;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static de.KnollFrank.lib.settingssearch.PreferenceScreensProviderTestHelper.getPreferenceScreenByTitle;
import static de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenGraphProvider1Test.createPreferenceScreenWithHostProvider;
import static de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenGraphProvider1Test.createSearchablePreferenceScreenGraphProviderAndPreferenceScreenWithHostProvider;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import androidx.test.core.app.ActivityScenario;

import com.google.common.collect.Iterables;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.common.graph.TreePath;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenGraphProvider1Test.Fragment3ConnectedToFragment4;
import de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenGraphProvider1Test.Fragment4;
import de.KnollFrank.settingssearch.test.TestActivity;

@RunWith(RobolectricTestRunner.class)
@SuppressWarnings({"UnstableApiUsage"})
public class TreePathInstantiatorTest {

    @Test
    public void test_instantiateTreePath_singleNode() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(fragmentActivity -> {
                // Given
                final TreePathInstantiator treePathInstantiator = new TreePathInstantiator(createPreferenceScreenWithHostProvider(fragmentActivity));
                final Tree<SearchablePreferenceScreen, SearchablePreference> pojoGraphSingleNode =
                        createSomePojoGraph(
                                fragmentActivity,
                                Fragment4.class);
                final SearchablePreferenceScreen searchablePreferenceScreen = Iterables.getOnlyElement(pojoGraphSingleNode.graph().nodes());
                final TreePath<SearchablePreferenceScreen, SearchablePreference> treePath =
                        new TreePath<>(pojoGraphSingleNode, List.of(searchablePreferenceScreen));

                // When
                final TreePath<PreferenceScreenWithHost, Preference> graphPathInstantiated = treePathInstantiator.instantiate(treePath);

                // Then
                assertSameSize(graphPathInstantiated, treePath);
                assertThat(graphPathInstantiated.endNode().host(), is(instanceOf(searchablePreferenceScreen.host())));
            });
        }
    }

    @Test
    public void test_instantiateTreePath_twoNodes() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(fragmentActivity -> {
                // Given
                final TreePathInstantiator treePathInstantiator = new TreePathInstantiator(createPreferenceScreenWithHostProvider(fragmentActivity));
                final Tree<SearchablePreferenceScreen, SearchablePreference> graphTwoNodes = createSomePojoGraph(fragmentActivity, Fragment3ConnectedToFragment4.class);
                final SearchablePreferenceScreen thirdScreen = getPreferenceScreenByTitle(graphTwoNodes.graph().nodes(), "third screen");
                final SearchablePreferenceScreen fourthScreen = getPreferenceScreenByTitle(graphTwoNodes.graph().nodes(), "fourth screen");
                final TreePath<SearchablePreferenceScreen, SearchablePreference> graphPath =
                        new TreePath<>(
                                graphTwoNodes,
                                List.of(thirdScreen, fourthScreen));

                // When
                final TreePath<PreferenceScreenWithHost, Preference> graphPathInstantiated = treePathInstantiator.instantiate(graphPath);

                // Then
                assertSameSize(graphPathInstantiated, graphPath);
                assertThat(graphPathInstantiated.startNode().host(), is(instanceOf(thirdScreen.host())));
                assertThat(graphPathInstantiated.endNode().host(), is(instanceOf(fourthScreen.host())));
            });
        }
    }

    private static Tree<SearchablePreferenceScreen, SearchablePreference> createSomePojoGraph(
            final FragmentActivity fragmentActivity,
            final Class<? extends Fragment> root) {
        final var result =
                createSearchablePreferenceScreenGraphProviderAndPreferenceScreenWithHostProvider(
                        root,
                        fragmentActivity);
        return result
                .searchablePreferenceScreenGraphProvider()
                .getSearchablePreferenceScreenGraph(result.preferenceScreenWithHost());
    }

    private static void assertSameSize(final TreePath<?, ?> actual, final TreePath<?, ?> expected) {
        assertThat(actual.nodes().size(), is(expected.nodes().size()));
    }
}