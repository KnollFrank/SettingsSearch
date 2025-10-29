package de.KnollFrank.lib.settingssearch.search;

import java.util.Set;

@FunctionalInterface
// FK-TODO: Suche nicht nur mit indexOf(), sondern ähnlich zur Suche in AndroidStudio, z.B. Strg-Alt-T, dann "mergedscreen" eingeben, es wird dann sogar MergedPreferenceScreen gefunden und die passenden Teile gehighlightet.
public interface StringMatcher {

    Set<IndexRange> getMatches(String haystack, String needle);
}
