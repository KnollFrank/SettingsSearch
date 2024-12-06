package de.KnollFrank.lib.settingssearch.results;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Function;

import de.KnollFrank.lib.settingssearch.common.compare.ComparatorFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

public class SearchablePreferencePOJOComparatorFactory {

    public static Comparator<SearchablePreferencePOJO> lexicographicalComparator() {
        return SearchablePreferencePOJOComparatorFactory
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
                ComparatorFactory.emptiesLast(String.CASE_INSENSITIVE_ORDER));
    }
}
