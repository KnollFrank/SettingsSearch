package com.bytehamster.lib.preferencesearch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.test.core.app.ActivityScenario;

import com.bytehamster.preferencesearch.R;
import com.bytehamster.preferencesearch.multiplePreferenceScreens.MultiplePreferenceScreensExample;
import com.bytehamster.preferencesearch.multiplePreferenceScreens.PrefsFragmentFirst;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.junit.Test;

public class PreferencesGraphProviderTest {

    @Test
    public void shouldGetPreferencesGraph() {
        // FK-TODO: do not use MultiplePreferenceScreensExample as an activity, create a new activity within the androidTest folder.
        try (final ActivityScenario<MultiplePreferenceScreensExample> scenario = ActivityScenario.launch(MultiplePreferenceScreensExample.class)) {
            scenario.onActivity(PreferencesGraphProviderTest::shouldGetPreferencesGraph);
        }
    }

    private static void shouldGetPreferencesGraph(final FragmentActivity activity) {
        // Given
        final PreferencesGraphProvider preferencesGraphProvider = new PreferencesGraphProvider(new PreferenceFragmentCompatHelper(activity, R.id.fragmentContainerView));
        final PreferenceFragmentCompat root = new PrefsFragmentFirst();

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

    private static PreferenceScreenWithHost getPreferenceScreenByName(final Graph<PreferenceScreenWithHost, DefaultEdge> preferencesGraph,
                                                                      final String name) {
        return preferencesGraph
                .vertexSet()
                .stream()
                .filter(preferenceScreen -> name.equals(preferenceScreen.preferenceScreen.toString()))
                .findFirst()
                .get();
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