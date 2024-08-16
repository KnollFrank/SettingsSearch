package de.KnollFrank.lib.settingssearch.search;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;

import androidx.preference.Preference;
import androidx.test.core.app.ActivityScenario;

import org.junit.Test;

import java.util.List;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.search.PreferenceMatch.Type;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoGetter;
import de.KnollFrank.settingssearch.test.TestActivity;

public class PreferenceMatcherTest {

    @Test
    public void shouldGetPreferenceMatches() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(context -> {
                // Given
                final Preference preference = new Preference(context);
                preference.setKey("feedback");
                preference.setTitle("Title, title part");
                preference.setSummary("title in summary");
                final SearchableInfoGetter searchableInfoGetter =
                        _preference ->
                                _preference.equals(preference) ?
                                        Optional.of("searchable info also has a title") :
                                        Optional.empty();

                // When
                final List<PreferenceMatch> preferenceMatches =
                        PreferenceMatcher.getPreferenceMatches(
                                preference,
                                "title",
                                searchableInfoGetter);

                // Then
                assertThat(
                        preferenceMatches,
                        hasItems(
                                new PreferenceMatch(preference, Type.TITLE, new IndexRange(0, 5)),
                                new PreferenceMatch(preference, Type.TITLE, new IndexRange(7, 12)),
                                new PreferenceMatch(preference, Type.SUMMARY, new IndexRange(0, 5)),
                                new PreferenceMatch(preference, Type.SEARCHABLE_INFO, new IndexRange(27, 32))));
            });
        }
    }
}