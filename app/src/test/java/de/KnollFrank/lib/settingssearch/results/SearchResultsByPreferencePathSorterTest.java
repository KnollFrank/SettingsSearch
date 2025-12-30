package de.KnollFrank.lib.settingssearch.results;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenTestFactory.createScreen;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceTestFactory.createPreference;

import org.jgrapht.Graph;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceTestFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceWithinGraph;

@RunWith(RobolectricTestRunner.class)
public class SearchResultsByPreferencePathSorterTest {

    // Fk-TODO: refactor?
    @Test
    public void shouldSortSearchResultsByPreferencePath() {
        // Given
        final SearchablePreference defaultSpeed = SearchablePreferenceTestFactory.createSearchablePreferenceWithTitle("default speed");

        final SearchablePreference car = createPreference("car");
        final SearchablePreference defaultSpeedOfCar = SearchablePreferenceTestFactory.createSearchablePreferenceWithTitle("default speed");

        final SearchablePreference walk = createPreference("walk");
        final SearchablePreference defaultSpeedOfWalk = SearchablePreferenceTestFactory.createSearchablePreferenceWithTitle("default speed");

        final SearchResultsSorter searchResultsSorter = new SearchResultsByPreferencePathSorter();

        final SearchablePreferenceScreen carScreen = createScreen("car", Set.of(car));
        final SearchablePreference toWalk = createPreference("toWalk");
        final SearchablePreferenceScreen rootScreen = createScreen("root", Set.of(defaultSpeed, toWalk));
        final SearchablePreferenceScreen walkScreen = createScreen("walk", Set.of(walk));
        final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph =
                SearchablePreferenceScreenGraphTestFactory
                        .createGraphBuilder()
                        .addEdge(rootScreen, carScreen, new SearchablePreferenceEdge(defaultSpeed))
                        .addEdge(rootScreen, walkScreen, new SearchablePreferenceEdge(toWalk))
                        .addEdge(carScreen, createScreen("defaultSpeedOfCar", Set.of(defaultSpeedOfCar)), new SearchablePreferenceEdge(car))
                        .addEdge(walkScreen, createScreen("defaultSpeedOfWalk", Set.of(defaultSpeedOfWalk)), new SearchablePreferenceEdge(walk))
                        .build();

        // When
        final SearchablePreferenceWithinGraph _defaultSpeedOfWalk = new SearchablePreferenceWithinGraph(defaultSpeedOfWalk, graph);
        final SearchablePreferenceWithinGraph _defaultSpeed = new SearchablePreferenceWithinGraph(defaultSpeed, graph);
        final SearchablePreferenceWithinGraph _defaultSpeedOfCar = new SearchablePreferenceWithinGraph(defaultSpeedOfCar, graph);
        final List<SearchablePreferenceWithinGraph> sortedSearchResults =
                searchResultsSorter.sort(
                        Set.of(
                                _defaultSpeedOfWalk,
                                _defaultSpeed,
                                _defaultSpeedOfCar));

        // Then
        assertThat(
                sortedSearchResults,
                contains(
                        _defaultSpeed,
                        _defaultSpeedOfCar,
                        _defaultSpeedOfWalk));
    }
}
