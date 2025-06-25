package de.KnollFrank.lib.settingssearch.results;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

@FunctionalInterface
public interface SearchResultsFilter {

    boolean includePreferenceInSearchResults(SearchablePreference preference);
}
