package de.KnollFrank.lib.settingssearch.results;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Function;

import de.KnollFrank.lib.settingssearch.common.compare.ComparatorFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

class SearchablePreferencePOJOComparatorFactory {

    public static Comparator<SearchablePreference> lexicographicalComparator() {
        return SearchablePreferencePOJOComparatorFactory
                .title()
                .thenComparing(summary())
                .thenComparing(searchableInfo());
    }

    private static Comparator<SearchablePreference> title() {
        return comparing(SearchablePreference::getTitle);
    }

    private static Comparator<SearchablePreference> summary() {
        return comparing(SearchablePreference::getSummary);
    }

    private static Comparator<SearchablePreference> searchableInfo() {
        return comparing(SearchablePreference::getSearchableInfo);
    }

    private static Comparator<SearchablePreference> comparing(final Function<SearchablePreference, Optional<String>> keyExtractor) {
        return Comparator.comparing(
                keyExtractor,
                ComparatorFactory.emptiesLast(String.CASE_INSENSITIVE_ORDER));
    }
}
