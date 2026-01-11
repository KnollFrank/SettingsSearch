package de.KnollFrank.lib.settingssearch.results;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceOfHostWithinTree;

@FunctionalInterface
public interface SearchResultsFilter {

    boolean includePreferenceInSearchResults(SearchablePreferenceOfHostWithinTree preference);
}
