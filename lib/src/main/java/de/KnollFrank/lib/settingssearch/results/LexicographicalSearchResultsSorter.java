package de.KnollFrank.lib.settingssearch.results;

import static de.KnollFrank.lib.settingssearch.results.SearchablePreferencePOJOComparatorFactory.lexicographicalComparator;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

public class LexicographicalSearchResultsSorter implements SearchResultsSorter {

    private static final Comparator<SearchablePreferencePOJO> COMPARATOR = lexicographicalComparator();

    @Override
    public List<SearchablePreferencePOJO> sort(final Collection<SearchablePreferencePOJO> searchResults) {
        return searchResults
                .stream()
                .sorted(COMPARATOR)
                .collect(Collectors.toList());
    }
}
