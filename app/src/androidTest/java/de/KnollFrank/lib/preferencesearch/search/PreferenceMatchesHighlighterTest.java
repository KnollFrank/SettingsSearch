package de.KnollFrank.lib.preferencesearch.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayWithSize;

import android.text.Spannable;

import androidx.preference.Preference;
import androidx.test.core.app.ActivityScenario;

import com.google.common.collect.ImmutableList;

import org.junit.Test;

import java.util.List;
import java.util.function.Supplier;

import de.KnollFrank.lib.preferencesearch.client.DefaultSearchableInfoAttribute;
import de.KnollFrank.lib.preferencesearch.search.PreferenceMatch.Type;
import de.KnollFrank.preferencesearch.test.TestActivity;

public class PreferenceMatchesHighlighterTest {

    @Test
    public void shouldHighlightPreferenceMatches() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(context -> {
                // Given
                final Preference preference = new Preference(context);
                preference.setKey("feedback");
                preference.setSummary("summary, summary");

                final Supplier<List<Object>> markupsFactory = () -> MarkupFactory.createMarkups(context);
                final List<PreferenceMatch> preferenceMatches =
                        ImmutableList.of(
                                new PreferenceMatch(preference, Type.SUMMARY, new IndexRange(0, 6)),
                                new PreferenceMatch(preference, Type.SUMMARY, new IndexRange(9, 15)));

                // When
                PreferenceMatchesHighlighter.highlight(
                        preferenceMatches,
                        markupsFactory,
                        new DefaultSearchableInfoAttribute());

                // Then
                final Spannable summary = (Spannable) preference.getSummary();
                final int markupsSize = markupsFactory.get().size();
                assertThat(summary.getSpans(0, 6, Object.class), arrayWithSize(markupsSize));
                assertThat(summary.getSpans(9, 15, Object.class), arrayWithSize(markupsSize));
            });
        }
    }
}