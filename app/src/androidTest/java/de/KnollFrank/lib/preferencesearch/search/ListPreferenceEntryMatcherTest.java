package de.KnollFrank.lib.preferencesearch.search;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import androidx.test.core.app.ActivityScenario;

import org.junit.Test;

import de.KnollFrank.preferencesearch.test.TestActivity;

public class ListPreferenceEntryMatcherTest {

    @Test
    public void test_matchesAnyEntry() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(context -> {
                // Given
                final String needle = "some ListPreference";
                final CharSequence[] entries = {"dummy entry", "entry of " + needle};

                // When
                final boolean matchesAnyEntry =
                        ListPreferenceEntryMatcher.matchesAnyEntry(entries, needle);

                // Then
                assertThat(matchesAnyEntry, is(true));
            });
        }
    }

    @Test
    public void test_matchesAnyEntry_noEntries() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(context -> {
                // Given
                final CharSequence[] entries = null;

                // When
                final boolean matchesAnyEntry =
                        ListPreferenceEntryMatcher.matchesAnyEntry(entries, "entry of");

                // Then
                assertThat(matchesAnyEntry, is(false));
            });
        }
    }
}
