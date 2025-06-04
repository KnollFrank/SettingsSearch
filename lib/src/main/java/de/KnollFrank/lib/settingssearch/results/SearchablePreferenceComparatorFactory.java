package de.KnollFrank.lib.settingssearch.results;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Function;

import de.KnollFrank.lib.settingssearch.common.compare.ComparatorFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;

class SearchablePreferenceComparatorFactory {

    public static Comparator<SearchablePreferenceEntity> lexicographicalComparator() {
        return SearchablePreferenceComparatorFactory
                .title()
                .thenComparing(summary())
                .thenComparing(searchableInfo());
    }

    private static Comparator<SearchablePreferenceEntity> title() {
        return comparing(SearchablePreferenceEntity::getTitle);
    }

    private static Comparator<SearchablePreferenceEntity> summary() {
        return comparing(SearchablePreferenceEntity::getSummary);
    }

    private static Comparator<SearchablePreferenceEntity> searchableInfo() {
        return comparing(SearchablePreferenceEntity::getSearchableInfo);
    }

    private static Comparator<SearchablePreferenceEntity> comparing(final Function<SearchablePreferenceEntity, Optional<String>> keyExtractor) {
        return Comparator.comparing(
                keyExtractor,
                ComparatorFactory.emptiesLast(String.CASE_INSENSITIVE_ORDER));
    }
}
