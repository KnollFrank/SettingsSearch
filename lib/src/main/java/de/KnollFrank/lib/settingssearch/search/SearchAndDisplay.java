package de.KnollFrank.lib.settingssearch.search;

import java.util.Set;

import de.KnollFrank.lib.settingssearch.common.LanguageCode;
import de.KnollFrank.lib.settingssearch.results.SearchResultsDisplayer;

class SearchAndDisplay<C> {

    private final PreferenceSearcher<C> preferenceSearcher;
    private final SearchResultsDisplayer searchResultsDisplayer;

    public SearchAndDisplay(final PreferenceSearcher<C> preferenceSearcher,
                            final SearchResultsDisplayer searchResultsDisplayer) {
        this.preferenceSearcher = preferenceSearcher;
        this.searchResultsDisplayer = searchResultsDisplayer;
    }

    public void searchForQueryAndDisplayResults(final String query,
                                                final LanguageCode languageCode,
                                                final C actualConfiguration) {
        final Set<PreferenceMatch> preferenceMatches = preferenceSearcher.searchFor(query, languageCode, actualConfiguration);
        searchResultsDisplayer.displaySearchResults(preferenceMatches);
    }
}
