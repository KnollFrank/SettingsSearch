package de.KnollFrank.lib.settingssearch.results;

import androidx.preference.PreferenceManager;

import de.KnollFrank.lib.settingssearch.results.recyclerview.SearchResultsFragment;
import de.KnollFrank.lib.settingssearch.search.MarkupFactory;

public class SearchResultsDisplayerFactory {

    public static SearchResultsDisplayer createSearchResultsDisplayer(
            final SearchResultsFragment searchResultsFragment,
            final PreferenceManager preferenceManager) {
        return new SearchResultsDisplayer(
                searchResultsFragment,
                () -> MarkupFactory.createMarkups(preferenceManager.getContext()));
    }
}
