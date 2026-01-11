package de.KnollFrank.lib.settingssearch.results;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenTestFactory.createScreen;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceTestFactory.createSearchablePreference;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceTestFactory.createSearchablePreferenceWithTitle;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceOfHostWithinTree;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory;

@RunWith(RobolectricTestRunner.class)
@SuppressWarnings({"UnstableApiUsage"})
public class SearchResultsByPreferencePathSorterTest {

    @Test
    public void shouldSortSearchResultsByPreferencePath() {
        // Given
        final SearchablePreference defaultSpeed = createSearchablePreferenceWithTitle("default speed");

        final SearchablePreference car = createSearchablePreference("car");
        final SearchablePreference defaultSpeedOfCar = createSearchablePreferenceWithTitle("default speed");

        final SearchablePreference walk = createSearchablePreference("walk");
        final SearchablePreference defaultSpeedOfWalk = createSearchablePreferenceWithTitle("default speed");

        final SearchResultsSorter searchResultsSorter = new SearchResultsByPreferencePathSorter();

        final SearchablePreferenceScreen carScreen = createScreen("car", Set.of(car));
        final SearchablePreference toWalk = createSearchablePreference("toWalk");
        final SearchablePreferenceScreen rootScreen = createScreen("root", Set.of(defaultSpeed, toWalk));
        final SearchablePreferenceScreen walkScreen = createScreen("walk", Set.of(walk));
        final SearchablePreferenceScreen defaultSpeedOfWalkScreen = createScreen("defaultSpeedOfWalk", Set.of(defaultSpeedOfWalk));
        final SearchablePreferenceScreen defaultSpeedOfCarScreen = createScreen("defaultSpeedOfCar", Set.of(defaultSpeedOfCar));
        final Tree<SearchablePreferenceScreen, SearchablePreference> graph =
                new Tree<>(
                        SearchablePreferenceScreenGraphTestFactory
                                .createGraphBuilder()
                                .putEdgeValue(rootScreen, carScreen, defaultSpeed)
                                .putEdgeValue(rootScreen, walkScreen, toWalk)
                                .putEdgeValue(carScreen, defaultSpeedOfCarScreen, car)
                                .putEdgeValue(walkScreen, defaultSpeedOfWalkScreen, walk)
                                .build());

        // When
        final SearchablePreferenceOfHostWithinTree _defaultSpeedOfWalk = new SearchablePreferenceOfHostWithinTree(defaultSpeedOfWalk, defaultSpeedOfWalkScreen, graph);
        final SearchablePreferenceOfHostWithinTree _defaultSpeed = new SearchablePreferenceOfHostWithinTree(defaultSpeed, rootScreen, graph);
        final SearchablePreferenceOfHostWithinTree _defaultSpeedOfCar = new SearchablePreferenceOfHostWithinTree(defaultSpeedOfCar, defaultSpeedOfCarScreen, graph);
        final List<SearchablePreferenceOfHostWithinTree> sortedSearchResults =
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
