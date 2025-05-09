package de.KnollFrank.lib.settingssearch.results;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static de.KnollFrank.lib.settingssearch.SearchablePreferenceScreenGraphProvider1Test.makeGetPreferencePathWorkOnPreferences;
import static de.KnollFrank.lib.settingssearch.db.preference.dao.POJOTestFactory.copyPreferenceAndSetPredecessor;
import static de.KnollFrank.lib.settingssearch.db.preference.dao.POJOTestFactory.createSearchablePreferencePOJO;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.SearchablePreferenceScreenGraphProvider1Test;
import de.KnollFrank.lib.settingssearch.db.preference.dao.TestPreferenceFragment;
import de.KnollFrank.lib.settingssearch.db.preference.db.AppDatabaseTest;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

@RunWith(RobolectricTestRunner.class)
public class SearchResultsByPreferencePathSorterTest extends AppDatabaseTest {

    @Test
    public void shouldSortSearchResultsByPreferencePath() {
        // Given
        final SearchablePreference defaultSpeed =
                createSearchablePreferencePOJO(
                        "default speed",
                        TestPreferenceFragment.class,
                        Optional.empty());

        final SearchablePreference car =
                createSearchablePreferencePOJO(
                        "car",
                        SearchablePreferenceScreenGraphProvider1Test.Fragment3.class,
                        Optional.empty());

        final SearchablePreference defaultSpeedOfCar = copyPreferenceAndSetPredecessor(defaultSpeed, Optional.of(car));

        final SearchablePreference walk =
                createSearchablePreferencePOJO(
                        "walk",
                        SearchablePreferenceScreenGraphProvider1Test.Fragment3.class,
                        Optional.empty());

        final SearchablePreference defaultSpeedOfWalk = copyPreferenceAndSetPredecessor(defaultSpeed, Optional.of(walk));

        makeGetPreferencePathWorkOnPreferences(
                List.of(car, walk, defaultSpeedOfWalk, defaultSpeed, defaultSpeedOfCar),
                appDatabase);
        final SearchResultsSorter searchResultsSorter = new SearchResultsByPreferencePathSorter();

        // When
        final List<SearchablePreference> sortedSearchResults =
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
