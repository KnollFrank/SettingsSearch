package de.KnollFrank.lib.settingssearch.search;

import android.widget.SearchView;

public class SearchForQueryAndDisplayResultsCommand {

    private final SearchAndDisplay searchAndDisplay;
    private final SearchView searchView;

    public SearchForQueryAndDisplayResultsCommand(final SearchAndDisplay searchAndDisplay,
                                                  final SearchView searchView) {
        this.searchAndDisplay = searchAndDisplay;
        this.searchView = searchView;
    }

    public void searchForQueryAndDisplayResults() {
        searchAndDisplay.searchForQueryAndDisplayResults(searchView.getQuery().toString());
    }
}
