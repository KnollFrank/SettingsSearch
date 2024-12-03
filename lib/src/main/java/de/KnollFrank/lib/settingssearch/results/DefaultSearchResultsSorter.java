package de.KnollFrank.lib.settingssearch.results;

import static de.KnollFrank.lib.settingssearch.results.SearchablePreferencePOJOComparatorFactory.searchablePreferencePOJOComparator;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

public class DefaultSearchResultsSorter implements SearchResultsSorter {

    private static final Comparator<SearchablePreferencePOJO> COMPARATOR = searchablePreferencePOJOComparator();

    @Override
    public List<SearchablePreferencePOJO> sort(final Collection<SearchablePreferencePOJO> preferences) {
        return preferences
                .stream()
                .sorted(COMPARATOR)
                .collect(Collectors.toList());
    }
}
