package de.KnollFrank.lib.settingssearch.search;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceTestFactory.createSearchablePreference;

import androidx.test.core.app.ActivityScenario;

import com.codepoetics.ambivalence.Either;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.common.converter.DrawableAndStringConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.settingssearch.R;
import de.KnollFrank.settingssearch.test.TestActivity;

@RunWith(RobolectricTestRunner.class)
public class PreferenceMatcherTest {

    @Test
    public void shouldGetPreferenceMatches() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                // Given
                final SearchablePreference searchablePreference =
                        createSearchablePreference(
                                Optional.of("Title, title part"),
                                Optional.of("title in summary"),
                                Optional.of("searchable info also has a title"),
                                Optional.of(Either.ofRight(DrawableAndStringConverter.drawable2String(activity.getResources().getDrawable(R.drawable.smiley, null)))));

                // When
                final Optional<PreferenceMatch> preferenceMatch =
                        PreferenceMatcher.getPreferenceMatch(
                                searchablePreference,
                                "title");

                // Then
                assertThat(
                        preferenceMatch,
                        is(Optional.of(
                                new PreferenceMatch(
                                        searchablePreference,
                                        Set.of(
                                                new IndexRange(0, 5),
                                                new IndexRange(7, 12)),
                                        Set.of(new IndexRange(0, 5)),
                                        Set.of(new IndexRange(27, 32))))));
            });
        }
    }
}