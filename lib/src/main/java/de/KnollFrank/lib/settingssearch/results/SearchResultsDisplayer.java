package de.KnollFrank.lib.settingssearch.results;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.results.recyclerview.SearchResultsFragment;
import de.KnollFrank.lib.settingssearch.search.PreferenceMatch;
import de.KnollFrank.lib.settingssearch.search.PreferenceMatchesHighlighter;

public class SearchResultsDisplayer {

    private final MarkupsFactory markupsFactory;
    private final SearchResultsFragment searchResultsFragment;
    private final SearchResultsFilter searchResultsFilter;
    private final SearchResultsSorter searchResultsSorter;

    public SearchResultsDisplayer(final SearchResultsFragment searchResultsFragment,
                                  final MarkupsFactory markupsFactory,
                                  final SearchResultsFilter searchResultsFilter,
                                  final SearchResultsSorter searchResultsSorter) {
        this.searchResultsFragment = searchResultsFragment;
        this.markupsFactory = markupsFactory;
        this.searchResultsFilter = searchResultsFilter;
        this.searchResultsSorter = searchResultsSorter;
    }

    public void displaySearchResults(final Set<PreferenceMatch> preferenceMatches) {
        new PreferenceMatchesHighlighter(markupsFactory).highlight(preferenceMatches);
        searchResultsFragment.setSearchResults(
                searchResultsSorter.sort(filter(getPreferences(preferenceMatches))));
    }

    public SearchResultsFragment getSearchResultsFragment() {
        return searchResultsFragment;
    }

    private static Set<SearchablePreferenceEntity> getPreferences(final Set<PreferenceMatch> preferenceMatches) {
        return preferenceMatches
                .stream()
                .map(PreferenceMatch::preference)
                .collect(Collectors.toSet());
    }

    private Collection<SearchablePreferenceEntity> filter(final Collection<SearchablePreferenceEntity> preferences) {
        return preferences
                .stream()
                .filter(searchResultsFilter::includePreferenceInSearchResults)
                .collect(Collectors.toList());
    }
}
