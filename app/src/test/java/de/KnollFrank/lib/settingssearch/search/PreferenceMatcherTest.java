package de.KnollFrank.lib.settingssearch.search;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceTestFactory.createSearchablePreference;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceWithinGraphTestFactory.createSearchablePreferenceWithinGraph;

import org.junit.Test;

import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

public class PreferenceMatcherTest {

    @Test
    public void shouldGetPreferenceMatches() {
        // Given
        final PreferenceMatcher preferenceMatcher = new PreferenceMatcher(new CaseInsensitiveSubstringMatcher());
        final SearchablePreference searchablePreference =
                createSearchablePreference(
                        Optional.of("Title, title part"),
                        Optional.of("title in summary"),
                        Optional.of("searchable info also has a title"),
                        Optional.empty());

        // When
        final Optional<PreferenceMatch> preferenceMatch =
                preferenceMatcher.getPreferenceMatch(
                        createSearchablePreferenceWithinGraph(searchablePreference),
                        "title");

        // Then
        assertThat(
                preferenceMatch,
                is(Optional.of(
                        new PreferenceMatch(
                                createSearchablePreferenceWithinGraph(searchablePreference),
                                Set.of(
                                        new IndexRange(0, 5),
                                        new IndexRange(7, 12)),
                                Set.of(new IndexRange(0, 5)),
                                Set.of(new IndexRange(27, 32))))));
    }
}