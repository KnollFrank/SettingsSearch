package de.KnollFrank.lib.settingssearch.results;

import static de.KnollFrank.lib.settingssearch.results.SearchablePreferenceComparatorFactory.lexicographicalComparator;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceWithinGraph;

class LexicographicalSearchResultsSorter implements SearchResultsSorter {

    private static final Comparator<SearchablePreferenceWithinGraph> COMPARATOR = lexicographicalComparator();

    @Override
    public List<SearchablePreferenceWithinGraph> sort(final Collection<SearchablePreferenceWithinGraph> searchResults) {
        return searchResults
                .stream()
                .sorted(COMPARATOR)
                .collect(Collectors.toList());
    }
}
