package de.KnollFrank.lib.settingssearch.results;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.PreferenceEntityPath;
import de.KnollFrank.lib.settingssearch.common.Lists;
import de.KnollFrank.lib.settingssearch.common.compare.ComparatorFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;

public class SearchResultsByPreferencePathSorter implements SearchResultsSorter {

    private final Function<SearchablePreferenceEntity, PreferenceEntityPath> getPreferencePath;

    public SearchResultsByPreferencePathSorter(final Function<SearchablePreferenceEntity, PreferenceEntityPath> getPreferencePath) {
        this.getPreferencePath = getPreferencePath;
    }

    @Override
    public List<SearchablePreferenceEntity> sort(final Collection<SearchablePreferenceEntity> searchResults) {
        return searchResults
                .stream()
                .sorted(getPreferenceByPreferencePathComparator())
                .collect(Collectors.toList());
    }

    private Comparator<SearchablePreferenceEntity> getPreferenceByPreferencePathComparator() {
        return Comparator.comparing(
                getPreferencePath,
                getPreferencePathComparator());
    }

    private static Comparator<PreferenceEntityPath> getPreferencePathComparator() {
        return Comparator.comparing(
                preferencePath -> Lists.reverse(preferencePath.preferences()),
                ComparatorFactory.lexicographicalListComparator(SearchablePreferenceComparatorFactory.lexicographicalComparator()));
    }
}
