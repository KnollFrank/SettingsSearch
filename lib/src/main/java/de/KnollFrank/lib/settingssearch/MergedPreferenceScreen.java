package de.KnollFrank.lib.settingssearch;

import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceEntityDAO;
import de.KnollFrank.lib.settingssearch.results.SearchResultsDisplayer;

public record MergedPreferenceScreen(SearchablePreferenceEntityDAO searchablePreferenceDAO,
                                     SearchResultsDisplayer searchResultsDisplayer) {
}
