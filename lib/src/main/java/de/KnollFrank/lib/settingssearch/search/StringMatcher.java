package de.KnollFrank.lib.settingssearch.search;

import java.util.Set;

@FunctionalInterface
public interface StringMatcher {

    Set<IndexRange> getMatches(String haystack, String needle);
}
