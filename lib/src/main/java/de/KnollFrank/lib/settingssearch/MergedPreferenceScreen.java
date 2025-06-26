package de.KnollFrank.lib.settingssearch;

import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceEntityDAO;
import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenGraphDAO;
import de.KnollFrank.lib.settingssearch.results.SearchResultsDisplayer;

public record MergedPreferenceScreen(
        // FK-TODO: remove searchablePreferenceDAO?
        SearchablePreferenceEntityDAO searchablePreferenceDAO,
        SearchablePreferenceScreenGraphDAO searchablePreferenceScreenGraphDAO,
        SearchResultsDisplayer searchResultsDisplayer) {
}
