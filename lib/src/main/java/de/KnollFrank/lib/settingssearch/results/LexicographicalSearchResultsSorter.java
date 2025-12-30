package de.KnollFrank.lib.settingssearch.results;

import static de.KnollFrank.lib.settingssearch.results.SearchablePreferenceComparatorFactory.lexicographicalComparator;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceOfHostWithinGraph;

class LexicographicalSearchResultsSorter implements SearchResultsSorter {

    private static final Comparator<SearchablePreferenceOfHostWithinGraph> COMPARATOR = lexicographicalComparator();

    @Override
    public List<SearchablePreferenceOfHostWithinGraph> sort(final Collection<SearchablePreferenceOfHostWithinGraph> searchResults) {
        return searchResults
                .stream()
                .sorted(COMPARATOR)
                .collect(Collectors.toList());
    }
}
