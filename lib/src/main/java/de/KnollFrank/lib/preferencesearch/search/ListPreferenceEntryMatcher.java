package de.KnollFrank.lib.preferencesearch.search;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class ListPreferenceEntryMatcher {

    public static boolean matchesAnyEntry(final CharSequence[] entries, final String needle) {
        if(entries == null) {
            return false;
        }
        return Arrays
                .stream(entries)
                .map(CharSequence::toString)
                .anyMatch(entry -> hasAnyPreferenceMatch(entry, needle));
    }

    private static boolean hasAnyPreferenceMatch(final String haystack, final String needle) {
        final List<PreferenceMatch> preferenceMatches =
                PreferenceMatcher.getPreferenceMatches(Optional.of(haystack), needle, indexRange -> null);
        return !preferenceMatches.isEmpty();
    }
}
