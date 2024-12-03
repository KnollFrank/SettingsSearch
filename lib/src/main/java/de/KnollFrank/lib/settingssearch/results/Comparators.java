package de.KnollFrank.lib.settingssearch.results;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Function;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

class Comparators {

    public static Comparator<SearchablePreferencePOJO> searchablePreferencePOJOComparator() {
        return Comparators
                .title()
                .thenComparing(summary())
                .thenComparing(searchableInfo());
    }

    private static Comparator<SearchablePreferencePOJO> title() {
        return comparing(SearchablePreferencePOJO::getTitle);
    }

    private static Comparator<SearchablePreferencePOJO> summary() {
        return comparing(SearchablePreferencePOJO::getSummary);
    }

    private static Comparator<SearchablePreferencePOJO> searchableInfo() {
        return comparing(SearchablePreferencePOJO::getSearchableInfo);
    }

    private static Comparator<SearchablePreferencePOJO> comparing(final Function<SearchablePreferencePOJO, Optional<String>> keyExtractor) {
        return Comparator.comparing(
                keyExtractor,
                emptiesLast(String.CASE_INSENSITIVE_ORDER));
    }

    private static <T extends Comparable<T>> Comparator<Optional<T>> emptiesLast(final Comparator<T> comparator) {
        return (optional1, optional2) -> {
            if (optional1.isEmpty() && optional2.isEmpty()) {
                return 0;
            } else if (optional1.isEmpty()) {
                return +1;
            } else if (optional2.isEmpty()) {
                return -1;
            } else {
                return comparator.compare(optional1.get(), optional2.get());
            }
        };
    }
}
