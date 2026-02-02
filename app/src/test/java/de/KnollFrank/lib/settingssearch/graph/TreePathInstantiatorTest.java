package de.KnollFrank.lib.settingssearch.graph;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static de.KnollFrank.lib.settingssearch.PreferenceScreensProviderTestHelper.getPreferenceScreenByTitle;
import static de.KnollFrank.lib.settingssearch.graph.SearchableTreeBuilder1Test.createPreferenceScreenWithHostProvider;
import static de.KnollFrank.lib.settingssearch.graph.SearchableTreeBuilder1Test.createSearchablePreferenceScreenGraphProviderAndPreferenceScreenWithHostProvider;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import androidx.test.core.app.ActivityScenario;

import com.google.common.collect.Iterables;
import com.google.common.graph.ImmutableValueGraph;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

import de.KnollFrank.lib.settingssearch.PreferenceScreenOfHostOfActivity;
import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.common.graph.TreePath;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.graph.SearchableTreeBuilder1Test.Fragment3ConnectedToFragment4;
import de.KnollFrank.lib.settingssearch.graph.SearchableTreeBuilder1Test.Fragment4;
import de.KnollFrank.settingssearch.test.TestActivity;

@RunWith(RobolectricTestRunner.class)
@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class TreePathInstantiatorTest {

    @Test
    public void test_instantiateTreePath_singleNode() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(fragmentActivity -> {
                // Given
                final TreePathInstantiator treePathInstantiator = new TreePathInstantiator(createPreferenceScreenWithHostProvider(fragmentActivity));
                final Tree<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> pojoGraphSingleNode =
                        createSomePojoGraph(
                                fragmentActivity,
                                Fragment4.class);
                final SearchablePreferenceScreen searchablePreferenceScreen = Iterables.getOnlyElement(pojoGraphSingleNode.graph().nodes());
                final TreePath<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> treePath =
                        new TreePath<>(pojoGraphSingleNode, List.of(searchablePreferenceScreen));

                // When
                final TreePath<PreferenceScreenOfHostOfActivity, Preference, ImmutableValueGraph<PreferenceScreenOfHostOfActivity, Preference>> treePathInstantiated = treePathInstantiator.instantiate(treePath);

                // Then
                assertSameSize(treePathInstantiated, treePath);
                assertThat(treePathInstantiated.endNode().hostOfPreferenceScreen(), is(instanceOf(searchablePreferenceScreen.host().preferenceFragmentClass())));
            });
        }
    }

    @Test
    public void test_instantiateTreePath_twoNodes() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(fragmentActivity -> {
                // Given
                final TreePathInstantiator treePathInstantiator = new TreePathInstantiator(createPreferenceScreenWithHostProvider(fragmentActivity));
                final Tree<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> graphTwoNodes = createSomePojoGraph(fragmentActivity, Fragment3ConnectedToFragment4.class);
                final SearchablePreferenceScreen thirdScreen = getPreferenceScreenByTitle(graphTwoNodes.graph().nodes(), "third screen");
                final SearchablePreferenceScreen fourthScreen = getPreferenceScreenByTitle(graphTwoNodes.graph().nodes(), "fourth screen");
                final TreePath<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> treePath =
                        new TreePath<>(
                                graphTwoNodes,
                                List.of(thirdScreen, fourthScreen));

                // When
                final TreePath<PreferenceScreenOfHostOfActivity, Preference, ImmutableValueGraph<PreferenceScreenOfHostOfActivity, Preference>> treePathInstantiated = treePathInstantiator.instantiate(treePath);

                // Then
                assertSameSize(treePathInstantiated, treePath);
                assertThat(treePathInstantiated.startNode().hostOfPreferenceScreen(), is(instanceOf(thirdScreen.host().preferenceFragmentClass())));
                assertThat(treePathInstantiated.endNode().hostOfPreferenceScreen(), is(instanceOf(fourthScreen.host().preferenceFragmentClass())));
            });
        }
    }

    private static Tree<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> createSomePojoGraph(
            final FragmentActivity fragmentActivity,
            final Class<? extends Fragment> root) {
        final var result =
                createSearchablePreferenceScreenGraphProviderAndPreferenceScreenWithHostProvider(
                        root,
                        fragmentActivity);
        return result
                .searchablePreferenceScreenTreeProvider()
                .getSearchablePreferenceScreenTree(result.preferenceScreenOfHostOfActivity());
    }

    private static void assertSameSize(final TreePath<?, ?, ?> actual, final TreePath<?, ?, ?> expected) {
        assertThat(actual.nodes().size(), is(expected.nodes().size()));
    }
}