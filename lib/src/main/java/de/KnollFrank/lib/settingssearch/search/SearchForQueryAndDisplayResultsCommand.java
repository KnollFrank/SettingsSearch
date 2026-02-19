package de.KnollFrank.lib.settingssearch.search;

import android.widget.SearchView;

import de.KnollFrank.lib.settingssearch.common.LanguageCode;

public class SearchForQueryAndDisplayResultsCommand<C> {

    private final SearchAndDisplay<C> searchAndDisplay;
    private final SearchView searchView;
    private final LanguageCode languageCode;
    private final C actualConfiguration;

    public SearchForQueryAndDisplayResultsCommand(final SearchAndDisplay<C> searchAndDisplay,
                                                  final SearchView searchView,
                                                  final LanguageCode languageCode,
                                                  final C actualConfiguration) {
        this.searchAndDisplay = searchAndDisplay;
        this.searchView = searchView;
        this.languageCode = languageCode;
        this.actualConfiguration = actualConfiguration;
    }

    public void searchForQueryAndDisplayResults() {
        searchAndDisplay.searchForQueryAndDisplayResults(
                searchView.getQuery().toString(),
                languageCode,
                actualConfiguration);
    }
}
