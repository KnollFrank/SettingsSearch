package de.KnollFrank.lib.settingssearch.client;

import de.KnollFrank.lib.settingssearch.provider.IncludePreferenceInSearchResultsPredicate;
import de.KnollFrank.lib.settingssearch.provider.PrepareShow;
import de.KnollFrank.lib.settingssearch.provider.ShowPreferencePathPredicate;
import de.KnollFrank.lib.settingssearch.results.MarkupFactory;
import de.KnollFrank.lib.settingssearch.results.SearchResultsFilter;
import de.KnollFrank.lib.settingssearch.results.SearchResultsSorter;
import de.KnollFrank.lib.settingssearch.results.recyclerview.PreferencePathDisplayer;
import de.KnollFrank.lib.settingssearch.search.ui.SearchPreferenceFragmentUI;
import de.KnollFrank.lib.settingssearch.search.ui.SearchResultsFragmentUI;

// FK-TODO: Suchergebnisse gruppierbar machen
public record SearchConfig(
        IncludePreferenceInSearchResultsPredicate includePreferenceInSearchResultsPredicate,
        ShowPreferencePathPredicate showPreferencePathPredicate,
        PrepareShow prepareShow,
        PreferencePathDisplayer preferencePathDisplayer,
        SearchResultsFilter searchResultsFilter,
        SearchResultsSorter searchResultsSorter,
        SearchPreferenceFragmentUI searchPreferenceFragmentUI,
        SearchResultsFragmentUI searchResultsFragmentUI,
        MarkupFactory markupFactory) {
}
