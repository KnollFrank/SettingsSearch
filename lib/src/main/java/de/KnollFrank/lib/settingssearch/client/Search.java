package de.KnollFrank.lib.settingssearch.client;

import de.KnollFrank.lib.settingssearch.provider.IncludePreferenceInSearchResultsPredicate;
import de.KnollFrank.lib.settingssearch.provider.PrepareShow;
import de.KnollFrank.lib.settingssearch.provider.ShowPreferencePathPredicate;
import de.KnollFrank.lib.settingssearch.results.SearchResultsSorter;
import de.KnollFrank.lib.settingssearch.search.ui.SearchPreferenceFragmentUI;
import de.KnollFrank.lib.settingssearch.search.ui.SearchResultsFragmentUI;

public record Search(
        IncludePreferenceInSearchResultsPredicate includePreferenceInSearchResultsPredicate,
        ShowPreferencePathPredicate showPreferencePathPredicate,
        PrepareShow prepareShow,
        SearchResultsSorter searchResultsSorter,
        SearchPreferenceFragmentUI searchPreferenceFragmentUI,
        SearchResultsFragmentUI searchResultsFragmentUI) {
}
