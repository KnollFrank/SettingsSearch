package de.KnollFrank.lib.settingssearch;

import de.KnollFrank.lib.settingssearch.db.preference.db.SearchablePreferenceScreenGraphRepository;
import de.KnollFrank.lib.settingssearch.results.SearchResultsDisplayer;

public record MergedPreferenceScreen<C>(
        SearchablePreferenceScreenGraphRepository<C> graphRepository,
        SearchResultsDisplayer searchResultsDisplayer) {
}
