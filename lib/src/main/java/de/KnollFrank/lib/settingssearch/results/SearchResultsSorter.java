package de.KnollFrank.lib.settingssearch.results;

import java.util.Collection;
import java.util.List;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;

@FunctionalInterface
public interface SearchResultsSorter {

    List<SearchablePreferenceEntity> sort(Collection<SearchablePreferenceEntity> searchResults);
}
