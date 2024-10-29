package de.KnollFrank.lib.settingssearch.search;

import java.util.List;

import de.KnollFrank.lib.settingssearch.results.SearchResultsDisplayer;

class SearchAndDisplay {

    private final PreferenceSearcher preferenceSearcher;
    private final SearchResultsDisplayer searchResultsDisplayer;

    public SearchAndDisplay(final PreferenceSearcher preferenceSearcher,
                            final SearchResultsDisplayer searchResultsDisplayer) {
        this.preferenceSearcher = preferenceSearcher;
        this.searchResultsDisplayer = searchResultsDisplayer;
    }

    public void searchForQueryAndDisplayResults(final String query) {
        final List<PreferenceMatch> preferenceMatches = preferenceSearcher.searchFor(query);
        searchResultsDisplayer.displaySearchResults(preferenceMatches, query);
    }
}
