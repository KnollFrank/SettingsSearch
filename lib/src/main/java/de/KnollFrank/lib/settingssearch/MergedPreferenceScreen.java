package de.KnollFrank.lib.settingssearch;

import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.results.SearchResultsDisplayer;

public record MergedPreferenceScreen(
        Set<SearchablePreference> preferences,
        SearchResultsDisplayer searchResultsDisplayer) {
}
