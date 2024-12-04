package de.KnollFrank.lib.settingssearch.results;

import java.util.Collection;
import java.util.List;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

@FunctionalInterface
public interface SearchResultsSorter {

    List<SearchablePreferencePOJO> sort(Collection<SearchablePreferencePOJO> searchResults);
}
