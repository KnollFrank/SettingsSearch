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
import de.KnollFrank.lib.preferencesearch.search.provider.SearchableInfoAttribute;
import de.KnollFrank.preferencesearch.test.TestActivity;

public class PreferenceMatchesHighlighterTest {

    @Test
    public void shouldHighlightPreferenceMatches_title() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(context -> {
                // Given
                final Preference preference = new Preference(context);
                preference.setKey("feedback");
                preference.setTitle("title, title");

                final Supplier<List<Object>> markupsFactory = () -> MarkupFactory.createMarkups(context);
                final List<PreferenceMatch> preferenceMatches =
                        ImmutableList.of(
                                new PreferenceMatch(preference, Type.TITLE, new IndexRange(0, 4)),
                                new PreferenceMatch(preference, Type.TITLE, new IndexRange(7, 11)));

                // When
                PreferenceMatchesHighlighter.highlight(
                        preferenceMatches,
                        markupsFactory,
                        new DefaultSearchableInfoAttribute());

                // Then
                final Spannable title = (Spannable) preference.getTitle();
                final int markupsSize = markupsFactory.get().size();
                assertThat(title.getSpans(0, 4, Object.class), arrayWithSize(markupsSize));
                assertThat(title.getSpans(7, 11, Object.class), arrayWithSize(markupsSize));
            });
        }
    }

    @Test
    public void shouldHighlightPreferenceMatches_summary() {
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

    @Test
    public void shouldHighlightPreferenceMatches_searchableInfo() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(context -> {
                // Given
                final Preference preference = new Preference(context);
                preference.setKey("feedback");
                final SearchableInfoAttribute searchableInfoAttribute = new DefaultSearchableInfoAttribute();
                searchableInfoAttribute.setSearchableInfo(preference, "info, info");

                final Supplier<List<Object>> markupsFactory = () -> MarkupFactory.createMarkups(context);
                final List<PreferenceMatch> preferenceMatches =
                        ImmutableList.of(
                                new PreferenceMatch(preference, Type.SEARCHABLE_INFO, new IndexRange(0, 3)),
                                new PreferenceMatch(preference, Type.SEARCHABLE_INFO, new IndexRange(6, 9)));

                // When
                PreferenceMatchesHighlighter.highlight(
                        preferenceMatches,
                        markupsFactory,
                        searchableInfoAttribute);

                // Then
                final Spannable searchableInfo =
                        (Spannable) searchableInfoAttribute
                                .getSearchableInfo(preference)
                                .get();
                final int markupsSize = markupsFactory.get().size();
                assertThat(searchableInfo.getSpans(0, 3, Object.class), arrayWithSize(markupsSize));
                assertThat(searchableInfo.getSpans(6, 9, Object.class), arrayWithSize(markupsSize));
            });
        }
    }
}