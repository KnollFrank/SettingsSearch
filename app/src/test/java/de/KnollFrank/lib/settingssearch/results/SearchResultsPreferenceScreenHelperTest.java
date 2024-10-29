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
import de.KnollFrank.lib.settingssearch.results.SearchResultsPreferenceScreenHelper.Info;
import de.KnollFrank.lib.settingssearch.search.IndexRange;
import de.KnollFrank.lib.settingssearch.search.PreferenceMatch;
import de.KnollFrank.lib.settingssearch.search.PreferenceMatch.Type;
import de.KnollFrank.settingssearch.test.TestActivity;

@RunWith(RobolectricTestRunner.class)
public class SearchResultsPreferenceScreenHelperTest {

    @Test
    public void shouldDisplaySearchResultsOnPreferenceScreen() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                // Given
                final String title = "Title, title part";
                final SearchResultsPreferenceScreenHelper searchResultsPreferenceScreenHelper =
                        new SearchResultsPreferenceScreenHelper(
                                createSomePreferenceFragment(activity).getPreferenceManager(),
                                null,
                                pojoEntityMap -> null,
                                activity);

                // When
                final Info info =
                        searchResultsPreferenceScreenHelper.displaySearchResults(
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
                                .getChildrenRecursively(info.preferenceScreenWithMap().preferenceScreen())
                                .stream()
                                .anyMatch(preference -> title.equals(preference.getTitle().toString())),
                        is(true));
            });
        }
    }
}
