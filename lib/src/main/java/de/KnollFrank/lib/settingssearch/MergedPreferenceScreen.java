package de.KnollFrank.lib.settingssearch;

import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenGraphDAO;
import de.KnollFrank.lib.settingssearch.results.SearchResultsDisplayer;

public record MergedPreferenceScreen(
        SearchablePreferenceScreenGraphDAO searchablePreferenceScreenGraphDAO,
        SearchResultsDisplayer searchResultsDisplayer) {
}
