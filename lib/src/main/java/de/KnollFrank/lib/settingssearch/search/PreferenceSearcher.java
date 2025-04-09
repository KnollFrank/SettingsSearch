package de.KnollFrank.lib.settingssearch.search;

import java.util.Set;

import de.KnollFrank.lib.settingssearch.SearchablePreferenceDAO;
import de.KnollFrank.lib.settingssearch.provider.IncludePreferenceInSearchResultsPredicate;

class PreferenceSearcher {

    // FK-TODO: SQL-Datenbank verwenden? (siehe Branch precompute-MergedPreferenceScreen-SQLite)
    private final SearchablePreferenceDAO searchablePreferenceDAO;
    private final IncludePreferenceInSearchResultsPredicate includePreferenceInSearchResultsPredicate;

    public PreferenceSearcher(final SearchablePreferenceDAO searchablePreferenceDAO,
                              final IncludePreferenceInSearchResultsPredicate includePreferenceInSearchResultsPredicate) {
        this.searchablePreferenceDAO = searchablePreferenceDAO;
        this.includePreferenceInSearchResultsPredicate = includePreferenceInSearchResultsPredicate;
    }

    public Set<PreferenceMatch> searchFor(final String needle) {
        return searchablePreferenceDAO.searchFor(needle, includePreferenceInSearchResultsPredicate);
    }
}
