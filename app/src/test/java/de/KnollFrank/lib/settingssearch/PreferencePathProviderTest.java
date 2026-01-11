package de.KnollFrank.lib.settingssearch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenTestFactory.createScreen;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceTestFactory.createSearchablePreference;

import org.junit.Test;

import java.util.Set;

import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceOfHostWithinGraph;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory;

@SuppressWarnings({"UnstableApiUsage"})
public class PreferencePathProviderTest {

    @Test
    public void shouldReturnPathForTargetInRootScreen() {
        // Given
        // Graph structure:
        // [ rootScreen { p1 } ]
        final SearchablePreference p1 = createSearchablePreference("P1");
        final SearchablePreferenceScreen rootScreen = createScreen("Root", Set.of(p1));

        final Tree<SearchablePreferenceScreen, SearchablePreference> tree =
                new Tree<>(
                        SearchablePreferenceScreenGraphTestFactory
                                .createGraphBuilder()
                                .addNode(rootScreen)
                                .build());
        final SearchablePreferenceOfHostWithinGraph _p1 =
                new SearchablePreferenceOfHostWithinGraph(
                        p1,
                        rootScreen,
                        tree);

        // When
        final PreferencePath path = PreferencePathProvider.getPreferencePath(_p1);

        // Then
        // The path should only contain P1 since it's located directly in the root screen
        assertThat(path.preferences(), contains(_p1));
    }

    @Test
    public void shouldReturnPathThroughMultipleScreens() {
        // Given
        // Graph structure:
        // [ rootScreen { prefToA } ] --prefToA--> [ screenA { targetPref } ]
        final SearchablePreference prefToA = createSearchablePreference("ToA");
        final SearchablePreferenceScreen rootScreen = createScreen("Root", Set.of(prefToA));

        final SearchablePreference targetPref = createSearchablePreference("Target");
        final SearchablePreferenceScreen screenA = createScreen("ScreenA", Set.of(targetPref));

        final Tree<SearchablePreferenceScreen, SearchablePreference> graph =
                new Tree<>(
                        SearchablePreferenceScreenGraphTestFactory
                                .createGraphBuilder()
                                .putEdgeValue(rootScreen, screenA, prefToA)
                                .build());
        final SearchablePreferenceOfHostWithinGraph _targetPref =
                new SearchablePreferenceOfHostWithinGraph(
                        targetPref,
                        screenA,
                        graph);

        // When
        final PreferencePath path = PreferencePathProvider.getPreferencePath(_targetPref);

        // Then
        // The path must contain the bridge preference leading to screenA and the target itself
        assertThat(
                path.preferences(),
                contains(
                        new SearchablePreferenceOfHostWithinGraph(prefToA, rootScreen, graph),
                        _targetPref));
    }

    @Test
    public void shouldReturnDeepPathThroughThreeScreens() {
        // Given
        // Graph structure:
        // [ root { bridge1 } ] --bridge1--> [ screenA { bridge2 } ] --bridge2--> [ screenB { target } ]
        final SearchablePreference bridge1 = createSearchablePreference("Bridge1");
        final SearchablePreference bridge2 = createSearchablePreference("Bridge2");
        final SearchablePreference target = createSearchablePreference("Target");

        final SearchablePreferenceScreen root = createScreen("Root", Set.of(bridge1));
        final SearchablePreferenceScreen screenA = createScreen("A", Set.of(bridge2));
        final SearchablePreferenceScreen screenB = createScreen("B", Set.of(target));

        final Tree<SearchablePreferenceScreen, SearchablePreference> tree =
                new Tree<>(
                        SearchablePreferenceScreenGraphTestFactory
                                .createGraphBuilder()
                                .putEdgeValue(root, screenA, bridge1)
                                .putEdgeValue(screenA, screenB, bridge2)
                                .build());
        final SearchablePreferenceOfHostWithinGraph _target =
                new SearchablePreferenceOfHostWithinGraph(
                        target,
                        screenB,
                        tree);

        // When
        final PreferencePath path = PreferencePathProvider.getPreferencePath(_target);

        // Then
        // The path traces the sequence of bridge preferences across all screens to the target: [Bridge1, Bridge2, Target]
        assertThat(
                path.preferences(),
                contains(
                        new SearchablePreferenceOfHostWithinGraph(bridge1, root, tree),
                        new SearchablePreferenceOfHostWithinGraph(bridge2, screenA, tree),
                        _target));
    }
}
