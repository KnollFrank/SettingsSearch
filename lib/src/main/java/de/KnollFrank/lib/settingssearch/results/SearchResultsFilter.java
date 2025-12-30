package de.KnollFrank.lib.settingssearch.results;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceWithinGraph;

@FunctionalInterface
public interface SearchResultsFilter {

    boolean includePreferenceInSearchResults(SearchablePreferenceWithinGraph preference);
}
