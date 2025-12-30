package de.KnollFrank.lib.settingssearch.results;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceOfHostWithinGraph;

@FunctionalInterface
public interface SearchResultsFilter {

    boolean includePreferenceInSearchResults(SearchablePreferenceOfHostWithinGraph preference);
}
