package com.bytehamster.lib.preferencesearch;

import static com.bytehamster.lib.preferencesearch.PreferencesGraphProviderTestHelper.configureConnectedPreferencesOfFragment;
import static com.bytehamster.lib.preferencesearch.PreferencesGraphProviderTestHelper.getPreferenceScreenByName;
import static com.bytehamster.preferencesearch.test.TestActivity.FRAGMENT_CONTAINER_VIEW;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.test.core.app.ActivityScenario;

import com.bytehamster.preferencesearch.test.TestActivity;
import com.google.common.collect.ImmutableList;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.junit.Test;

public class PreferencesGraphProvider1Test {

    @Test
    public void shouldGetPreferencesGraph() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(PreferencesGraphProvider1Test::shouldGetPreferencesGraph);
        }
    }

    private static void shouldGetPreferencesGraph(final FragmentActivity activity) {
        // Given
        final PreferencesGraphProvider preferencesGraphProvider =
                new PreferencesGraphProvider(
                        new PreferenceFragments(activity, FRAGMENT_CONTAINER_VIEW));
        final PreferenceFragmentCompat root = new Fragment1ConnectedToFragment2AndFragment4();

        // When
        final Graph<PreferenceScreenWithHost, DefaultEdge> preferencesGraph = preferencesGraphProvider.getPreferencesGraph(root);

        // Then
        assertThat(
                GraphHelper.equalsGraphs(
                        preferencesGraph,
                        getPreferencesGraphExpected(
                                getPreferenceScreenByName(preferencesGraph, "first screen"),
                                getPreferenceScreenByName(preferencesGraph, "second screen"),
                                getPreferenceScreenByName(preferencesGraph, "third screen"),
                                getPreferenceScreenByName(preferencesGraph, "fourth screen"))),
                is(true));
    }

    public static class Fragment1ConnectedToFragment2AndFragment4 extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
            configureConnectedPreferencesOfFragment(
                    this,
                    "first screen",
                    ImmutableList.of(
                            Fragment2ConnectedToFragment3.class,
                            Fragment4.class));
        }
    }

    public static class Fragment2ConnectedToFragment3 extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
            configureConnectedPreferencesOfFragment(
                    this,
                    "second screen",
                    ImmutableList.of(Fragment3.class));
        }
    }

    public static class Fragment3 extends BaseSearchPreferenceFragment {

        @Override
        public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
            configureConnectedPreferencesOfFragment(
                    this,
                    "third screen",
                    ImmutableList.of());
        }
    }

    public static class Fragment4 extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
            configureConnectedPreferencesOfFragment(
                    this,
                    "fourth screen",
                    ImmutableList.of());
        }
    }

    private static Graph<PreferenceScreenWithHost, DefaultEdge> getPreferencesGraphExpected(
            final PreferenceScreenWithHost screen1,
            final PreferenceScreenWithHost screen2,
            final PreferenceScreenWithHost screen3,
            final PreferenceScreenWithHost screen4) {
        final Graph<PreferenceScreenWithHost, DefaultEdge> graph = new DefaultDirectedGraph<>(DefaultEdge.class);

        graph.addVertex(screen1);
        graph.addVertex(screen2);
        graph.addVertex(screen3);
        graph.addVertex(screen4);

        graph.addEdge(screen1, screen2);
        graph.addEdge(screen1, screen4);
        graph.addEdge(screen2, screen3);

        return graph;
    }
}