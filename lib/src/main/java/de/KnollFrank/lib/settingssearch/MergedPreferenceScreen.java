package de.KnollFrank.lib.settingssearch;

import de.KnollFrank.lib.settingssearch.db.preference.db.SearchablePreferenceScreenTreeRepository;
import de.KnollFrank.lib.settingssearch.results.SearchResultsDisplayer;

public record MergedPreferenceScreen<C>(
        SearchablePreferenceScreenTreeRepository<C> treeRepository,
        SearchResultsDisplayer searchResultsDisplayer) {
}
