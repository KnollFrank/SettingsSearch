package de.KnollFrank.lib.settingssearch.search;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;

import androidx.test.core.app.ActivityScenario;

import com.codepoetics.ambivalence.Either;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.common.converter.DrawableAndStringConverter;
import de.KnollFrank.lib.settingssearch.db.preference.dao.POJOTestFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.search.PreferenceMatch.Type;
import de.KnollFrank.settingssearch.R;
import de.KnollFrank.settingssearch.test.TestActivity;

@RunWith(RobolectricTestRunner.class)
public class PreferenceMatcherTest {

    @Test
    public void shouldGetPreferenceMatches() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                // Given
                final SearchablePreferencePOJO searchablePreferencePOJO =
                        POJOTestFactory.createSearchablePreferencePOJO(
                                Optional.of("Title, title part"),
                                Optional.of("title in summary"),
                                Optional.of("searchable info also has a title"),
                                Optional.of(Either.ofRight(DrawableAndStringConverter.drawable2String(activity.getResources().getDrawable(R.drawable.smiley, null)))));

                // When
                final List<PreferenceMatch> preferenceMatches =
                        PreferenceMatcher.getPreferenceMatches(
                                searchablePreferencePOJO,
                                "title");

                // Then
                assertThat(
                        preferenceMatches,
                        hasItems(
                                new PreferenceMatch(searchablePreferencePOJO, Type.TITLE, new IndexRange(0, 5)),
                                new PreferenceMatch(searchablePreferencePOJO, Type.TITLE, new IndexRange(7, 12)),
                                new PreferenceMatch(searchablePreferencePOJO, Type.SUMMARY, new IndexRange(0, 5)),
                                new PreferenceMatch(searchablePreferencePOJO, Type.SEARCHABLE_INFO, new IndexRange(27, 32))));
            });
        }
    }
}