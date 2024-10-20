package de.KnollFrank.lib.settingssearch.results;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceFromPOJOConverterTest.createSomePreferenceFragment;

import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.test.core.app.ActivityScenario;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.db.preference.dao.POJOTestFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.search.IndexRange;
import de.KnollFrank.lib.settingssearch.search.PreferenceMatch;
import de.KnollFrank.settingssearch.test.TestActivity;

@RunWith(RobolectricTestRunner.class)
public class SearchResultsPreferenceScreenTest {

    @Test
    public void shouldDisplaySearchResultsOnPreferenceScreen() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                // Given
                final String title = "Title, title part";
                final PreferenceScreen preferenceScreen = createSomePreferenceFragment(activity).getPreferenceScreen();
                final SearchResultsPreferenceScreen searchResultsPreferenceScreen =
                        new SearchResultsPreferenceScreen(preferenceScreen);

                // When
                searchResultsPreferenceScreen.displayPreferenceMatchesOnPreferenceScreen(
                        List.of(
                                new PreferenceMatch(
                                        POJOTestFactory.createSearchablePreferencePOJO(
                                                title,
                                                "some summary",
                                                "searchable info also has a title"),
                                        PreferenceMatch.Type.TITLE,
                                        new IndexRange(0, 5))));

                // Then
                assertThat(
                        Preferences
                                .getAllChildren(preferenceScreen)
                                .stream()
                                .anyMatch(preference -> title.equals(preference.getTitle().toString())),
                        is(true));
            });
        }
    }

    @Test
    public void test_displayPreferenceMatchesOnPreferenceScreen_prefilledPreferenceScreen() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                // Given
                final PreferenceScreen prefilledPreferenceScreen = createSomePreferenceFragment(activity).getPreferenceScreen();
                prefilledPreferenceScreen.addPreference(new Preference(activity));

                final SearchResultsPreferenceScreen searchResultsPreferenceScreen =
                        new SearchResultsPreferenceScreen(prefilledPreferenceScreen);

                // When
                searchResultsPreferenceScreen.displayPreferenceMatchesOnPreferenceScreen(
                        List.of(createSomePreferenceMatch()));

                // Then
                assertThat(Preferences.getAllChildren(prefilledPreferenceScreen), hasSize(1));
            });
        }
    }

    private static PreferenceMatch createSomePreferenceMatch() {
        return createSomePreferenceMatch(POJOTestFactory.createSearchablePreferencePOJO(
                "Title, title part",
                "some summary",
                "searchable info also has a title"));
    }

    private static PreferenceMatch createSomePreferenceMatch(final SearchablePreferencePOJO searchablePreferencePOJO) {
        return new PreferenceMatch(
                searchablePreferencePOJO,
                PreferenceMatch.Type.TITLE,
                new IndexRange(0, 5));
    }
}
