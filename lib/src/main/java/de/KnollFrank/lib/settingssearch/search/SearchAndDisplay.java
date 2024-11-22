package de.KnollFrank.lib.settingssearch.search;

import java.util.Set;

import de.KnollFrank.lib.settingssearch.results.SearchResultsDisplayer;

class SearchAndDisplay {

    private final PreferenceSearcher preferenceSearcher;
    private final SearchResultsDisplayer searchResultsDisplayer;

    public SearchAndDisplay(final PreferenceSearcher preferenceSearcher,
                            final SearchResultsDisplayer searchResultsDisplayer) {
        this.preferenceSearcher = preferenceSearcher;
        this.searchResultsDisplayer = searchResultsDisplayer;
    }

    // FK-TODO: es dauert sehr lange, bis ein Suchergebnis angezeigt wird.
    public void searchForQueryAndDisplayResults(final String query) {
        final Set<PreferenceMatch> preferenceMatches = preferenceSearcher.searchFor(query);
        searchResultsDisplayer.displaySearchResults(preferenceMatches);
    }
}
