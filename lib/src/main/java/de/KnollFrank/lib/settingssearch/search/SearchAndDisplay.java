package de.KnollFrank.lib.settingssearch.search;

import java.util.Locale;
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

    public void searchForQueryAndDisplayResults(final String query, final Locale locale) {
        final Set<PreferenceMatch> preferenceMatches = preferenceSearcher.searchFor(query, locale);
        searchResultsDisplayer.displaySearchResults(preferenceMatches);
    }
}
