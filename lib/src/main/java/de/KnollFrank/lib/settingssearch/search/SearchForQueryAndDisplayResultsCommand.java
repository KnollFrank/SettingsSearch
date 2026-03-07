package de.KnollFrank.lib.settingssearch.search;

import android.widget.SearchView;

import java.util.Locale;

public class SearchForQueryAndDisplayResultsCommand<C> {

    private final SearchAndDisplay<C> searchAndDisplay;
    private final SearchView searchView;
    private final Locale locale;
    private final C actualConfiguration;

    public SearchForQueryAndDisplayResultsCommand(final SearchAndDisplay<C> searchAndDisplay,
                                                  final SearchView searchView,
                                                  final Locale locale,
                                                  final C actualConfiguration) {
        this.searchAndDisplay = searchAndDisplay;
        this.searchView = searchView;
        this.locale = locale;
        this.actualConfiguration = actualConfiguration;
    }

    public void searchForQueryAndDisplayResults() {
        searchAndDisplay.searchForQueryAndDisplayResults(
                searchView.getQuery().toString(),
                locale,
                actualConfiguration);
    }
}
