package de.KnollFrank.lib.settingssearch.search;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Strings;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

class PreferenceMatcher {

    public static Optional<PreferenceMatch> getPreferenceMatch(
            final SearchablePreferencePOJO haystack,
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

    private static Set<IndexRange> getMatches(final Optional<String> haystack, final String needle) {
        return haystack
                .map(_haystack -> getMatches(_haystack, needle))
                .orElseGet(Collections::emptySet);
    }

    private static Set<IndexRange> getMatches(final String haystack, final String needle) {
        return Strings
                .getIndicesOfNeedleWithinHaystack(haystack.toLowerCase(), needle.toLowerCase())
                .stream()
                .map(index -> new IndexRange(index, index + needle.length()))
                .collect(Collectors.toSet());
    }
}
