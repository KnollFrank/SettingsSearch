package de.KnollFrank.lib.settingssearch.search;

import android.widget.SearchView;

import java.util.Locale;

public class SearchForQueryAndDisplayResultsCommand {

    private final SearchAndDisplay searchAndDisplay;
    private final SearchView searchView;
    private final Locale locale;

    public SearchForQueryAndDisplayResultsCommand(final SearchAndDisplay searchAndDisplay,
                                                  final SearchView searchView,
                                                  final Locale locale) {
        this.searchAndDisplay = searchAndDisplay;
        this.searchView = searchView;
        this.locale = locale;
    }

    public void searchForQueryAndDisplayResults() {
        searchAndDisplay.searchForQueryAndDisplayResults(
                searchView.getQuery().toString(),
                locale);
    }
}
