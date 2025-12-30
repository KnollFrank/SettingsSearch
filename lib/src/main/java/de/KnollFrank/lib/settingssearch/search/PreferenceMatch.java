package de.KnollFrank.lib.settingssearch.search;

import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceOfHostWithinGraph;

public record PreferenceMatch(SearchablePreferenceOfHostWithinGraph preference,
                              Set<IndexRange> titleMatches,
                              Set<IndexRange> summaryMatches,
                              Set<IndexRange> searchableInfoMatches) {
}
