package de.KnollFrank.lib.settingssearch.results;

import static de.KnollFrank.lib.settingssearch.results.SearchablePreferencePOJOComparatorFactory.lexicographicalComparator;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

class LexicographicalSearchResultsSorter implements SearchResultsSorter {

    private static final Comparator<SearchablePreference> COMPARATOR = lexicographicalComparator();

    @Override
    public List<SearchablePreference> sort(final Collection<SearchablePreference> searchResults) {
        return searchResults
                .stream()
                .sorted(COMPARATOR)
                .collect(Collectors.toList());
    }
}
