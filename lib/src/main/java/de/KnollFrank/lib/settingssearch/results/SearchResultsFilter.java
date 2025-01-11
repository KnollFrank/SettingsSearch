package de.KnollFrank.lib.settingssearch.results;

import java.util.Collection;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

@FunctionalInterface
public interface SearchResultsFilter {

    Collection<SearchablePreference> filter(Collection<SearchablePreference> searchResults);
}
