package com.bytehamster.lib.preferencesearch;

import static com.bytehamster.lib.preferencesearch.PreferencesGraphProviderTestHelper.configureConnectedPreferencesOfFragment;
import static com.bytehamster.lib.preferencesearch.PreferencesGraphProviderTestHelper.getPreferenceScreenByName;
import static com.bytehamster.preferencesearch.test.TestActivity.FRAGMENT_CONTAINER_VIEW;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.test.core.app.ActivityScenario;

import com.bytehamster.preferencesearch.test.TestActivity;
import com.google.common.collect.ImmutableList;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.junit.Test;

public class PreferencesGraphProvider2Test {

    @Test
    public void shouldIgnoreNonPreferenceFragments() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(PreferencesGraphProvider2Test::shouldIgnoreNonPreferenceFragments);
        }
    }

    private static void shouldIgnoreNonPreferenceFragments(final FragmentActivity activity) {
        // Given
        final PreferencesGraphProvider preferencesGraphProvider =
                new PreferencesGraphProvider(
                        new PreferenceFragments(activity, FRAGMENT_CONTAINER_VIEW));
        final PreferenceFragmentCompat root = new FragmentConnectedToNonPreferenceFragment();

        // When
        final Graph<PreferenceScreenWithHost, DefaultEdge> preferencesGraph = preferencesGraphProvider.getPreferencesGraph(root);

        // Then
        assertThat(
                GraphHelper.equalsGraphs(
                        preferencesGraph,
                        DefaultDirectedGraph
                                .<PreferenceScreenWithHost, DefaultEdge>createBuilder(DefaultEdge.class)
                                .addVertex(getPreferenceScreenByName(preferencesGraph, "first screen"))
                                .build()),
                is(true));
    }

    public static class FragmentConnectedToNonPreferenceFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
            configureConnectedPreferencesOfFragment(
                    this,
                    "first screen",
                    ImmutableList.of(NonPreferenceFragment.class));
        }
    }

    public static class NonPreferenceFragment extends Fragment {
    }
}