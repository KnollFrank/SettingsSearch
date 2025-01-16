package de.KnollFrank.lib.settingssearch.results;

import de.KnollFrank.lib.settingssearch.results.recyclerview.SearchResultsFragment;

public class SearchResultsDisplayerFactory {

    public static SearchResultsDisplayer createSearchResultsDisplayer(
            final SearchResultsFragment searchResultsFragment,
            final MarkupsFactory markupsFactory,
            final SearchResultsFilter searchResultsFilter,
            final SearchResultsSorter searchResultsSorter) {
        return new SearchResultsDisplayer(
                searchResultsFragment,
                markupsFactory,
                searchResultsFilter,
                searchResultsSorter);
    }
}
