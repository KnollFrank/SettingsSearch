package com.bytehamster.lib.preferencesearch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.test.core.app.ActivityScenario;

import com.bytehamster.preferencesearch.multiplePreferenceScreens.MultiplePreferenceScreensExample;
import com.bytehamster.preferencesearch.multiplePreferenceScreens.PrefsFragmentFirst;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

public class PreferencesGraphProviderTest {

    @Test
    public void shouldGetPreferencesGraph() {
        try (final ActivityScenario<MultiplePreferenceScreensExample> scenario = ActivityScenario.launch(MultiplePreferenceScreensExample.class)) {
            scenario.onActivity(
                    activity -> {
                        // Given
                        final PreferencesGraphProvider preferencesGraphProvider =
                                new PreferencesGraphProvider(activity.getSupportFragmentManager());
                        final PreferenceFragmentCompat root = new PrefsFragmentFirst();

                        // When
                        final Graph<PreferenceScreen, DefaultEdge> preferencesGraph =
                                preferencesGraphProvider.getPreferencesGraph(root);

                        // Then
                        final List<String> preferenceScreens =
                                preferencesGraph
                                        .vertexSet()
                                        .stream()
                                        .map(Preference::toString)
                                        .collect(Collectors.toList());
                        assertThat(preferenceScreens, contains(root.getPreferenceScreen().toString()));
                    });
        }
    }
}