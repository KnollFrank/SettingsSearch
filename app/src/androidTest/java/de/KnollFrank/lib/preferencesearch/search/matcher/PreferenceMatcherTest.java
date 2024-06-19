package de.KnollFrank.lib.preferencesearch.search.matcher;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;

import android.content.Context;

import androidx.preference.Preference;
import androidx.test.core.app.ActivityScenario;

import org.junit.Test;

import java.util.List;

import de.KnollFrank.lib.preferencesearch.search.matcher.Match.Type;
import de.KnollFrank.preferencesearch.test.TestActivity;

public class PreferenceMatcherTest {

    @Test
    public void testMatches() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(PreferenceMatcherTest::testMatches);
        }
    }

    private static void testMatches(final Context context) {
        // Given
        final Preference preference = new Preference(context);
        preference.setKey("feedback");
        preference.setTitle("Title, title part");
        preference.setSummary("title in summary");

        // When
        final List<Match> matches = PreferenceMatcher.getMatches(preference, "title");

        // Then
        assertThat(
                matches,
                hasItems(
                        new Match(Type.TITLE, 0, 5),
                        new Match(Type.TITLE, 7, 12),
                        new Match(Type.SUMMARY, 0, 5)));
    }
}