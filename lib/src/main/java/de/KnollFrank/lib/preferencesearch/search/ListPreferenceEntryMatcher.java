package de.KnollFrank.lib.preferencesearch.search;

import java.util.List;
import java.util.Optional;

class ListPreferenceEntryMatcher {

    public static boolean matches(final String haystack, final String needle) {
        final List<PreferenceMatch> preferenceMatches =
                PreferenceMatcher.getPreferenceMatches(Optional.of(haystack), needle, indexRange -> null);
        return !preferenceMatches.isEmpty();
    }
}
