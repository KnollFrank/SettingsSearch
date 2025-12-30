package de.KnollFrank.lib.settingssearch.results;

import java.util.Collection;
import java.util.List;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceOfHostWithinGraph;

@FunctionalInterface
public interface SearchResultsSorter {

    List<SearchablePreferenceOfHostWithinGraph> sort(Collection<SearchablePreferenceOfHostWithinGraph> searchResults);
}
