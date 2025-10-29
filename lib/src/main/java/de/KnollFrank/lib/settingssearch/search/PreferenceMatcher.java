package de.KnollFrank.lib.settingssearch.search;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

public class PreferenceMatcher {

    private final StringMatcher stringMatcher;

    public PreferenceMatcher(final StringMatcher stringMatcher) {
        this.stringMatcher = stringMatcher;
    }

    public Optional<PreferenceMatch> getPreferenceMatch(final SearchablePreference haystack,
                                                        final String needle) {
        final Set<IndexRange> titleMatches = getMatches(haystack.getTitle(), needle);
        final Set<IndexRange> summaryMatches = getMatches(haystack.getSummary(), needle);
        final Set<IndexRange> searchableInfoMatches = getMatches(haystack.getSearchableInfo(), needle);
        return !titleMatches.isEmpty() || !summaryMatches.isEmpty() || !searchableInfoMatches.isEmpty() ?
                Optional.of(
                        new PreferenceMatch(
                                haystack,
                                titleMatches,
                                summaryMatches,
                                searchableInfoMatches)) :
                Optional.empty();
    }

    private Set<IndexRange> getMatches(final Optional<String> haystack, final String needle) {
        return haystack
                .map(_haystack -> stringMatcher.getMatches(_haystack, needle))
                .orElseGet(Collections::emptySet);
    }
}
