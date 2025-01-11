package de.KnollFrank.lib.settingssearch.results;

import android.content.Context;

import de.KnollFrank.lib.settingssearch.results.recyclerview.SearchResultsFragment;
import de.KnollFrank.lib.settingssearch.search.MarkupFactory;

public class SearchResultsDisplayerFactory {

    public static SearchResultsDisplayer createSearchResultsDisplayer(
            final SearchResultsFragment searchResultsFragment,
            final Context context,
            final SearchResultsFilter searchResultsFilter,
            final SearchResultsSorter searchResultsSorter) {
        return new SearchResultsDisplayer(
                searchResultsFragment,
                () -> MarkupFactory.createMarkups(context),
                searchResultsFilter,
                searchResultsSorter);
    }
}
