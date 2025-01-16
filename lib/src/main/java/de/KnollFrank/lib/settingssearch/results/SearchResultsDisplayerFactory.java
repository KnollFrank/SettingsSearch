package de.KnollFrank.lib.settingssearch.results;

import de.KnollFrank.lib.settingssearch.results.recyclerview.SearchResultsFragment;

public class SearchResultsDisplayerFactory {

    public static SearchResultsDisplayer createSearchResultsDisplayer(
            final SearchResultsFragment searchResultsFragment,
            final MarkupFactory markupFactory,
            final SearchResultsFilter searchResultsFilter,
            final SearchResultsSorter searchResultsSorter) {
        return new SearchResultsDisplayer(
                searchResultsFragment,
                markupFactory::createMarkups,
                searchResultsFilter,
                searchResultsSorter);
    }
}
