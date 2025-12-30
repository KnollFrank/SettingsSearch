package de.KnollFrank.lib.settingssearch.results;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Function;

import de.KnollFrank.lib.settingssearch.common.compare.ComparatorFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceWithinGraph;

class SearchablePreferenceComparatorFactory {

    public static Comparator<SearchablePreferenceWithinGraph> lexicographicalComparator() {
        return SearchablePreferenceComparatorFactory
                .title()
                .thenComparing(summary())
                .thenComparing(searchableInfo());
    }

    private static Comparator<SearchablePreferenceWithinGraph> title() {
        return comparing(SearchablePreference::getTitle);
    }

    private static Comparator<SearchablePreferenceWithinGraph> summary() {
        return comparing(SearchablePreference::getSummary);
    }

    private static Comparator<SearchablePreferenceWithinGraph> searchableInfo() {
        return comparing(SearchablePreference::getSearchableInfo);
    }

    private static Comparator<SearchablePreferenceWithinGraph> comparing(final Function<SearchablePreference, Optional<String>> keyExtractor) {
        return Comparator.comparing(
                getSearchablePreference().andThen(keyExtractor),
                ComparatorFactory.emptiesLast(String.CASE_INSENSITIVE_ORDER));
    }

    private static Function<SearchablePreferenceWithinGraph, SearchablePreference> getSearchablePreference() {
        return SearchablePreferenceWithinGraph::searchablePreference;
    }
}
