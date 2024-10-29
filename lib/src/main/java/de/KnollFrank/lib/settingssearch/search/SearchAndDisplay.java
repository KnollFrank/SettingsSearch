package de.KnollFrank.lib.settingssearch.search;

import java.util.List;

import de.KnollFrank.lib.settingssearch.results.SearchResultsPreferenceScreenHelper;

class SearchAndDisplay {

    private final PreferenceSearcher preferenceSearcher;
    private final SearchResultsPreferenceScreenHelper searchResultsPreferenceScreenHelper;

    public SearchAndDisplay(final PreferenceSearcher preferenceSearcher,
                            final SearchResultsPreferenceScreenHelper searchResultsPreferenceScreenHelper) {
        this.preferenceSearcher = preferenceSearcher;
        this.searchResultsPreferenceScreenHelper = searchResultsPreferenceScreenHelper;
    }

    public void searchForQueryAndDisplayResults(final String query) {
        final List<PreferenceMatch> preferenceMatches = preferenceSearcher.searchFor(query);
        searchResultsPreferenceScreenHelper.displaySearchResults(preferenceMatches);
    }
}
