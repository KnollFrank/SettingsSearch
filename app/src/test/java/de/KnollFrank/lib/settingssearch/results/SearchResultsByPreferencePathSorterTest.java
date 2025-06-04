package de.KnollFrank.lib.settingssearch.results;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static de.KnollFrank.lib.settingssearch.SearchablePreferenceScreenGraphProvider1Test.makeGetPreferencePathWorkOnPreferences;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceTestFactory.copyPreferenceAndSetPredecessor;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceTestFactory.createSearchablePreference;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.db.AppDatabaseTest;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;

@RunWith(RobolectricTestRunner.class)
public class SearchResultsByPreferencePathSorterTest extends AppDatabaseTest {

    @Test
    public void shouldSortSearchResultsByPreferencePath() {
        // Given
        final SearchablePreferenceEntity defaultSpeed =
                createSearchablePreference(
                        "default speed",
                        Optional.empty());

        final SearchablePreferenceEntity car =
                createSearchablePreference(
                        "car",
                        Optional.empty());

        final SearchablePreferenceEntity defaultSpeedOfCar = copyPreferenceAndSetPredecessor(defaultSpeed, Optional.of(car));

        final SearchablePreferenceEntity walk =
                createSearchablePreference(
                        "walk",
                        Optional.empty());

        final SearchablePreferenceEntity defaultSpeedOfWalk = copyPreferenceAndSetPredecessor(defaultSpeed, Optional.of(walk));

        makeGetPreferencePathWorkOnPreferences(
                List.of(car, walk, defaultSpeedOfWalk, defaultSpeed, defaultSpeedOfCar),
                appDatabase);
        final SearchResultsSorter searchResultsSorter = new SearchResultsByPreferencePathSorter(searchablePreference -> searchablePreference.getPreferencePath(appDatabase.searchablePreferenceDAO()));

        // When
        final List<SearchablePreferenceEntity> sortedSearchResults =
                searchResultsSorter.sort(
                        Set.of(
                                defaultSpeedOfWalk,
                                defaultSpeed,
                                defaultSpeedOfCar));

        // Then
        assertThat(
                sortedSearchResults,
                contains(
                        defaultSpeed,
                        defaultSpeedOfCar,
                        defaultSpeedOfWalk));
    }
}
