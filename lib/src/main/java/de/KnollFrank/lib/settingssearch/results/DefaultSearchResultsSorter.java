package de.KnollFrank.lib.settingssearch.results;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

public class DefaultSearchResultsSorter implements SearchResultsSorter {

    @Override
    public List<SearchablePreferencePOJO> sort(final Collection<SearchablePreferencePOJO> preferences) {
        return preferences
                .stream()
                .sorted(Comparators.searchablePreferencePOJOComparator())
                .collect(Collectors.toList());
    }
}
