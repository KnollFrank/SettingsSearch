package de.KnollFrank.lib.settingssearch.results;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static de.KnollFrank.lib.settingssearch.db.preference.dao.POJOTestFactory.copy;
import static de.KnollFrank.lib.settingssearch.db.preference.dao.POJOTestFactory.createSearchablePreferencePOJO;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.SearchablePreferenceScreenGraphProvider1Test;
import de.KnollFrank.lib.settingssearch.db.preference.dao.TestPreferenceFragment;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

@RunWith(RobolectricTestRunner.class)
public class SearchResultsByPreferencePathSorterTest {

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

        final SearchablePreference defaultSpeedOfCar = copy(defaultSpeed, Optional.of(car));

        final SearchablePreference walk =
                createSearchablePreferencePOJO(
                        "walk",
                        SearchablePreferenceScreenGraphProvider1Test.Fragment3.class,
                        Optional.empty());

        final SearchablePreference defaultSpeedOfWalk = copy(defaultSpeed, Optional.of(walk));

        final Collection<SearchablePreference> searchResults =
                List.of(
                        defaultSpeedOfWalk,
                        defaultSpeed,
                        defaultSpeedOfCar);
        final SearchResultsSorter searchResultsSorter = new SearchResultsByPreferencePathSorter();

        // When
        final List<SearchablePreference> sortedSearchResults = searchResultsSorter.sort(searchResults);

        // Then
        assertThat(
                sortedSearchResults,
                contains(
                        defaultSpeed,
                        defaultSpeedOfCar,
                        defaultSpeedOfWalk));
    }
}
