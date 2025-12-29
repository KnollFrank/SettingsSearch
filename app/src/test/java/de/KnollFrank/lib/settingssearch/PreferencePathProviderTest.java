package de.KnollFrank.lib.settingssearch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.builder.GraphBuilder;
import org.junit.Test;

import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

public class PreferencePathProviderTest {

    @Test
    public void shouldReturnPathForTargetInRootScreen() {
        // Given
        // Graph structure:
        // [ rootScreen { p1 } ]
        final SearchablePreference p1 = createPreference("P1");
        final SearchablePreferenceScreen rootScreen = createScreen("Root", Set.of(p1));

        final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph =
                PreferencePathProviderTest
                        .createGraphBuilder()
                        .addVertex(rootScreen)
                        .build();

        // When
        final PreferencePath path = PreferencePathProvider.getPreferencePath(graph, p1);

        // Then
        // The path should only contain P1 since it's located directly in the root screen
        assertThat(path.preferences(), contains(p1));
    }

    @Test
    public void shouldReturnPathThroughMultipleScreens() {
        // Given
        // Graph structure:
        // [ rootScreen { prefToA } ] --prefToA--> [ screenA { targetPref } ]
        final SearchablePreference prefToA = createPreference("ToA");
        final SearchablePreferenceScreen rootScreen = createScreen("Root", Set.of(prefToA));

        final SearchablePreference targetPref = createPreference("Target");
        final SearchablePreferenceScreen screenA = createScreen("ScreenA", Set.of(targetPref));

        final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph =
                PreferencePathProviderTest
                        .createGraphBuilder()
                        .addVertices(rootScreen, screenA)
                        .addEdge(rootScreen, screenA, new SearchablePreferenceEdge(prefToA))
                        .build();

        // When
        final PreferencePath path = PreferencePathProvider.getPreferencePath(graph, targetPref);

        // Then
        // The path must contain the bridge preference leading to screenA and the target itself
        assertThat(path.preferences(), contains(prefToA, targetPref));
    }

    @Test
    public void shouldReturnDeepPathThroughThreeScreens() {
        // Given
        // Graph structure:
        // [ root { bridge1 } ] --bridge1--> [ screenA { bridge2 } ] --bridge2--> [ screenB { target } ]
        final SearchablePreference bridge1 = createPreference("Bridge1");
        final SearchablePreference bridge2 = createPreference("Bridge2");
        final SearchablePreference target = createPreference("Target");

        final SearchablePreferenceScreen root = createScreen("Root", Set.of(bridge1));
        final SearchablePreferenceScreen screenA = createScreen("A", Set.of(bridge2));
        final SearchablePreferenceScreen screenB = createScreen("B", Set.of(target));

        final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph =
                PreferencePathProviderTest
                        .createGraphBuilder()
                        .addVertices(root, screenA, screenB)
                        .addEdge(root, screenA, new SearchablePreferenceEdge(bridge1))
                        .addEdge(screenA, screenB, new SearchablePreferenceEdge(bridge2))
                        .build();

        // When
        final PreferencePath path = PreferencePathProvider.getPreferencePath(graph, target);

        // Then
        // The path traces the sequence of bridge preferences across all screens to the target: [Bridge1, Bridge2, Target]
        assertThat(path.preferences(), contains(bridge1, bridge2, target));
    }

    private static GraphBuilder<SearchablePreferenceScreen, SearchablePreferenceEdge, ? extends DefaultDirectedGraph<SearchablePreferenceScreen, SearchablePreferenceEdge>> createGraphBuilder() {
        return DefaultDirectedGraph.createBuilder(SearchablePreferenceEdge.class);
    }

    private SearchablePreference createPreference(final String key) {
        return new SearchablePreference(
                "id-" + key,
                key,
                Optional.of("Title " + key),
                Optional.empty(),
                Optional.empty(),
                0,
                0,
                Optional.empty(),
                Optional.empty(),
                true,
                null,
                Optional.empty(),
                Set.of(),
                Optional.empty());
    }

    private SearchablePreferenceScreen createScreen(final String id,
                                                    final Set<SearchablePreference> preferences) {
        return new SearchablePreferenceScreen(
                id,
                null,
                Optional.of("Screen " + id),
                Optional.empty(),
                preferences);
    }
}
