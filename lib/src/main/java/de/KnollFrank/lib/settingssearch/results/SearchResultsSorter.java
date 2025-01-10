package de.KnollFrank.lib.settingssearch.results;

import java.util.Collection;
import java.util.List;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

@FunctionalInterface
public interface SearchResultsSorter {

    List<SearchablePreference> sort(Collection<SearchablePreference> searchResults);
}
