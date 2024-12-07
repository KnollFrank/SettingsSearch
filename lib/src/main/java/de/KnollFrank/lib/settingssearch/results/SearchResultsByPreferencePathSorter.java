package de.KnollFrank.lib.settingssearch.results;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.common.Lists;
import de.KnollFrank.lib.settingssearch.common.compare.ComparatorFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

public class SearchResultsByPreferencePathSorter implements SearchResultsSorter {

    private static final Comparator<SearchablePreferencePOJO> PREFERENCE_BY_PREFERENCE_PATH_COMPARATOR = getPreferenceByPreferencePathComparator();

    @Override
    public List<SearchablePreferencePOJO> sort(final Collection<SearchablePreferencePOJO> searchResults) {
        return searchResults
                .stream()
                .sorted(PREFERENCE_BY_PREFERENCE_PATH_COMPARATOR)
                .collect(Collectors.toList());
    }

    private static Comparator<SearchablePreferencePOJO> getPreferenceByPreferencePathComparator() {
        return Comparator.comparing(
                SearchablePreferencePOJO::getPreferencePath,
                getPreferencePathComparator());
    }

    private static Comparator<PreferencePath> getPreferencePathComparator() {
        return Comparator.comparing(
                preferencePath -> Lists.reverse(preferencePath.preferences()),
                ComparatorFactory.lexicographicalListComparator(SearchablePreferencePOJOComparatorFactory.lexicographicalComparator()));
    }
}
