package de.KnollFrank.lib.settingssearch.results;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Function;

import de.KnollFrank.lib.settingssearch.common.compare.ComparatorFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceOfHostWithinTree;

class SearchablePreferenceComparatorFactory {

    private SearchablePreferenceComparatorFactory() {
    }

    public static Comparator<SearchablePreferenceOfHostWithinTree> lexicographicalComparator() {
        return SearchablePreferenceComparatorFactory
                .title()
                .thenComparing(summary())
                .thenComparing(searchableInfo());
    }

    private static Comparator<SearchablePreferenceOfHostWithinTree> title() {
        return comparing(SearchablePreference::getTitle);
    }

    private static Comparator<SearchablePreferenceOfHostWithinTree> summary() {
        return comparing(SearchablePreference::getSummary);
    }

    private static Comparator<SearchablePreferenceOfHostWithinTree> searchableInfo() {
        return comparing(SearchablePreference::getSearchableInfo);
    }

    private static Comparator<SearchablePreferenceOfHostWithinTree> comparing(final Function<SearchablePreference, Optional<String>> keyExtractor) {
        return Comparator.comparing(
                getSearchablePreference().andThen(keyExtractor),
                ComparatorFactory.emptiesLast(String.CASE_INSENSITIVE_ORDER));
    }

    private static Function<SearchablePreferenceOfHostWithinTree, SearchablePreference> getSearchablePreference() {
        return SearchablePreferenceOfHostWithinTree::searchablePreference;
    }
}
