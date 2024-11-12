package de.KnollFrank.lib.settingssearch.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayWithSize;
import static de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreference2POJOConverter.SearchablePreferencePOJOWithMap;

import android.text.Spannable;

import androidx.test.core.app.ActivityScenario;

import com.google.common.collect.ImmutableList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.converter.IdGenerator;
import de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreference2POJOConverter;
import de.KnollFrank.lib.settingssearch.search.PreferenceMatch.Type;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoAttribute;
import de.KnollFrank.settingssearch.test.TestActivity;

@RunWith(RobolectricTestRunner.class)
public class PreferenceMatchesHighlighterTest {

    @Test
    public void shouldHighlightPreferenceMatches_title() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(context -> {
                // Given
                final SearchablePreference preference =
                        new SearchablePreference(
                                context,
                                Optional.empty(),
                                Optional.empty());
                preference.setKey("feedback");
                preference.setTitle("title, title");
                final SearchablePreferencePOJOWithMap searchablePreferencePOJOWithMap =
                        SearchablePreference2POJOConverter.convert2POJO(
                                preference,
                                new IdGenerator());

                final Supplier<List<Object>> markupsFactory = () -> MarkupFactory.createMarkups(context);
                final List<PreferenceMatch> preferenceMatches =
                        ImmutableList.of(
                                new PreferenceMatch(
                                        searchablePreferencePOJOWithMap.searchablePreferencePOJO(),
                                        Type.TITLE,
                                        new IndexRange(0, 5)),
                                new PreferenceMatch(
                                        searchablePreferencePOJOWithMap.searchablePreferencePOJO(),
                                        Type.TITLE,
                                        new IndexRange(7, 12)));
                final PreferenceMatchesHighlighter preferenceMatchesHighlighter =
                        new PreferenceMatchesHighlighter(
                                markupsFactory,
                                searchablePreferencePOJOWithMap.pojoEntityMap(),
                                new SearchableInfoAttribute());

                // When
                preferenceMatchesHighlighter.highlight(preferenceMatches);

                // Then
                final Spannable title = (Spannable) preference.getTitle();
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
                        new SearchablePreference(
                                context,
                                Optional.empty(),
                                Optional.empty());
                preference.setKey("feedback");
                preference.setSummary("summary, summary");
                final SearchablePreferencePOJOWithMap searchablePreferencePOJOWithMap =
                        SearchablePreference2POJOConverter.convert2POJO(
                                preference,
                                new IdGenerator());

                final Supplier<List<Object>> markupsFactory = () -> MarkupFactory.createMarkups(context);
                final List<PreferenceMatch> preferenceMatches =
                        ImmutableList.of(
                                new PreferenceMatch(
                                        searchablePreferencePOJOWithMap.searchablePreferencePOJO(),
                                        Type.SUMMARY,
                                        new IndexRange(0, 7)),
                                new PreferenceMatch(
                                        searchablePreferencePOJOWithMap.searchablePreferencePOJO(),
                                        Type.SUMMARY,
                                        new IndexRange(9, 16)));
                final PreferenceMatchesHighlighter preferenceMatchesHighlighter =
                        new PreferenceMatchesHighlighter(
                                markupsFactory,
                                searchablePreferencePOJOWithMap.pojoEntityMap(),
                                new SearchableInfoAttribute());

                // When
                preferenceMatchesHighlighter.highlight(preferenceMatches);

                // Then
                final Spannable summary = (Spannable) preference.getSummary();
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
                        new SearchablePreference(
                                context,
                                Optional.of(_searchableInfo),
                                Optional.empty());
                preference.setKey("feedback");
                final SearchableInfoAttribute searchableInfoAttribute = new SearchableInfoAttribute();
                searchableInfoAttribute.setSearchableInfo(preference, _searchableInfo);
                final SearchablePreferencePOJOWithMap searchablePreferencePOJOWithMap =
                        SearchablePreference2POJOConverter.convert2POJO(
                                preference,
                                new IdGenerator());

                final Supplier<List<Object>> markupsFactory = () -> MarkupFactory.createMarkups(context);
                final List<PreferenceMatch> preferenceMatches =
                        ImmutableList.of(
                                new PreferenceMatch(
                                        searchablePreferencePOJOWithMap.searchablePreferencePOJO(),
                                        Type.SEARCHABLE_INFO,
                                        new IndexRange(0, 4)),
                                new PreferenceMatch(
                                        searchablePreferencePOJOWithMap.searchablePreferencePOJO(),
                                        Type.SEARCHABLE_INFO,
                                        new IndexRange(6, 10)));
                final PreferenceMatchesHighlighter preferenceMatchesHighlighter =
                        new PreferenceMatchesHighlighter(
                                markupsFactory,
                                searchablePreferencePOJOWithMap.pojoEntityMap(),
                                searchableInfoAttribute);

                // When
                preferenceMatchesHighlighter.highlight(preferenceMatches);

                // Then
                final Spannable searchableInfo =
                        (Spannable) searchableInfoAttribute
                                .getSearchableInfo(preference)
                                .orElseThrow();
                final int markupsSize = markupsFactory.get().size();
                assertThat(searchableInfo.getSpans(0, 4, Object.class), arrayWithSize(markupsSize));
                assertThat(searchableInfo.getSpans(6, 10, Object.class), arrayWithSize(markupsSize));
            });
        }
    }
}