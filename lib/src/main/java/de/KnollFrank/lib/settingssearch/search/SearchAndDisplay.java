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

    // FK-TODO: es dauert sehr lange, bis ein Suchergebnis angezeigt wird. Mit dem Profiler Ursache finden. Vermutung: es könnte helfen, alle Preferences in den Speicher zu laden als fertigen PreferenceScreen und Suchtreffer darin einblenden oder ausblenden?
    public void searchForQueryAndDisplayResults(final String query) {
        final List<PreferenceMatch> preferenceMatches = preferenceSearcher.searchFor(query);
        searchResultsDisplayer.displaySearchResults(preferenceMatches, query);
    }
}
