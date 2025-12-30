package de.KnollFrank.lib.settingssearch.results;

import java.util.Collection;
import java.util.List;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceWithinGraph;

@FunctionalInterface
public interface SearchResultsSorter {

    List<SearchablePreferenceWithinGraph> sort(Collection<SearchablePreferenceWithinGraph> searchResults);
}
