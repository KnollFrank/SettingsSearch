package de.KnollFrank.lib.settingssearch.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayWithSize;

import android.text.Spannable;

import androidx.test.core.app.ActivityScenario;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import de.KnollFrank.lib.settingssearch.db.preference.dao.POJOTestFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.results.DefaultMarkupFactory;
import de.KnollFrank.settingssearch.test.TestActivity;

@RunWith(RobolectricTestRunner.class)
public class PreferenceMatchesHighlighterTest {

    @Test
    public void shouldHighlightPreferenceMatches_title() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(context -> {
                // Given
                final SearchablePreference preference =
                        POJOTestFactory.createSearchablePreferencePOJO(
                                Optional.of("title, title"),
                                Optional.empty(),
                                Optional.empty(),
                                Optional.empty());
                final Supplier<List<Object>> markupsFactory = () -> new DefaultMarkupFactory(context).createMarkups();
                final Set<PreferenceMatch> preferenceMatches =
                        Set.of(
                                new PreferenceMatch(
                                        preference,
                                        Set.of(
                                                new IndexRange(0, 5),
                                                new IndexRange(7, 12)),
                                        Set.of(),
                                        Set.of()));
                final PreferenceMatchesHighlighter preferenceMatchesHighlighter =
                        new PreferenceMatchesHighlighter(markupsFactory);

                // When
                preferenceMatchesHighlighter.highlight(preferenceMatches);

                // Then
                final Spannable title = (Spannable) preference.getHighlightedTitle().orElseThrow();
                final int markupsSize = markupsFactory.get().size();
                assertThat(title.getSpans(0, 5, Object.class), arrayWithSize(markupsSize));
                assertThat(title.getSpans(7, 12, Object.class), arrayWithSize(markupsSize));
            });
        }
    }

    @Test
    public void shouldHighlightPreferenceMatches_summary() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(context -> {
                // Given
                final SearchablePreference preference =
                        POJOTestFactory.createSearchablePreferencePOJO(
                                Optional.empty(),
                                Optional.of("summary, summary"),
                                Optional.empty(),
                                Optional.empty());
                final Supplier<List<Object>> markupsFactory = () -> new DefaultMarkupFactory(context).createMarkups();
                final Set<PreferenceMatch> preferenceMatches =
                        Set.of(
                                new PreferenceMatch(
                                        preference,
                                        Set.of(),
                                        Set.of(
                                                new IndexRange(0, 7),
                                                new IndexRange(9, 16)),
                                        Set.of()));
                final PreferenceMatchesHighlighter preferenceMatchesHighlighter =
                        new PreferenceMatchesHighlighter(markupsFactory);

                // When
                preferenceMatchesHighlighter.highlight(preferenceMatches);

                // Then
                final Spannable summary = (Spannable) preference.getHighlightedSummary().orElseThrow();
                final int markupsSize = markupsFactory.get().size();
                assertThat(summary.getSpans(0, 7, Object.class), arrayWithSize(markupsSize));
                assertThat(summary.getSpans(9, 16, Object.class), arrayWithSize(markupsSize));
            });
        }
    }

    @Test
    public void shouldHighlightPreferenceMatches_searchableInfo() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(context -> {
                // Given
                final String _searchableInfo = "info, info";
                final SearchablePreference preference =
                        POJOTestFactory.createSearchablePreferencePOJO(
                                Optional.empty(),
                                Optional.empty(),
                                Optional.of(_searchableInfo),
                                Optional.empty());
                final Supplier<List<Object>> markupsFactory = () -> new DefaultMarkupFactory(context).createMarkups();
                final Set<PreferenceMatch> preferenceMatches =
                        Set.of(
                                new PreferenceMatch(
                                        preference,
                                        Set.of(),
                                        Set.of(),
                                        Set.of(
                                                new IndexRange(0, 4),
                                                new IndexRange(6, 10))));
                final PreferenceMatchesHighlighter preferenceMatchesHighlighter =
                        new PreferenceMatchesHighlighter(markupsFactory);

                // When
                preferenceMatchesHighlighter.highlight(preferenceMatches);

                // Then
                final Spannable searchableInfo = (Spannable) preference.getHighlightedSearchableInfo().orElseThrow();
                final int markupsSize = markupsFactory.get().size();
                assertThat(searchableInfo.getSpans(0, 4, Object.class), arrayWithSize(markupsSize));
                assertThat(searchableInfo.getSpans(6, 10, Object.class), arrayWithSize(markupsSize));
            });
        }
    }
}