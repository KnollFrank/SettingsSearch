package de.KnollFrank.lib.settingssearch.search;

import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

public record PreferenceMatch(SearchablePreferencePOJO preference,
                              Set<IndexRange> titleMatches,
                              Set<IndexRange> summaryMatches,
                              Set<IndexRange> searchableInfoMatches) {
}
