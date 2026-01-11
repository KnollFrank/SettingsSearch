package de.KnollFrank.lib.settingssearch.results;

import java.util.Collection;
import java.util.List;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceOfHostWithinTree;

@FunctionalInterface
public interface SearchResultsSorter {

    List<SearchablePreferenceOfHostWithinTree> sort(Collection<SearchablePreferenceOfHostWithinTree> searchResults);
}
