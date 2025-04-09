package de.KnollFrank.lib.settingssearch;

import de.KnollFrank.lib.settingssearch.results.SearchResultsDisplayer;

public record MergedPreferenceScreen(
        SearchablePreferenceDAO searchablePreferenceDAO,
        SearchResultsDisplayer searchResultsDisplayer) {
}
