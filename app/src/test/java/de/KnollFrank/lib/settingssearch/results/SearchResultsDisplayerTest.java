package de.KnollFrank.lib.settingssearch.results;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceFromPOJOConverterTest.createSomePreferenceFragment;

import androidx.test.core.app.ActivityScenario;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.db.preference.dao.POJOTestFactory;
import de.KnollFrank.lib.settingssearch.search.IndexRange;
import de.KnollFrank.lib.settingssearch.search.PreferenceMatch;
import de.KnollFrank.lib.settingssearch.search.PreferenceMatch.Type;
import de.KnollFrank.settingssearch.test.TestActivity;

@RunWith(RobolectricTestRunner.class)
public class SearchResultsDisplayerTest {

    @Test
    public void shouldDisplaySearchResultsOnPreferenceScreen() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                // Given
                final String title = "Title, title part";
                final SearchResultsDisplayer searchResultsDisplayer =
                        SearchResultsPreferenceScreenHelperFactory.createSearchResultsPreferenceScreenHelper(
                                createSomePreferenceFragment(activity).getPreferenceManager(),
                                pojoEntityMap -> null);

                // When
                final SearchResultsDescription searchResultsDescription =
                        searchResultsDisplayer.displaySearchResults(
                                List.of(
                                        new PreferenceMatch(
                                                POJOTestFactory.createSearchablePreferencePOJO(
                                                        Optional.of(title),
                                                        Optional.of("some summary"),
                                                        Optional.of("searchable info also has a title")),
                                                Type.TITLE,
                                                new IndexRange(0, 5))),
                                "Title");

                // Then
                assertThat(
                        Preferences
                                .getChildrenRecursively(searchResultsDescription.preferenceScreenWithMap().preferenceScreen())
                                .stream()
                                .anyMatch(preference -> title.equals(preference.getTitle().toString())),
                        is(true));
            });
        }
    }
}
