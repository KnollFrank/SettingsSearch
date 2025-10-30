package de.KnollFrank.lib.settingssearch.results;

import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.results.recyclerview.SearchResultsFragment;
import de.KnollFrank.lib.settingssearch.search.PreferenceMatch;
import de.KnollFrank.lib.settingssearch.search.PreferenceMatchesHighlighter;

public class SearchResultsDisplayer {

    private final MarkupsFactory markupsFactory;
    private final SearchResultsFragment searchResultsFragment;
    private final SearchResultsSorter searchResultsSorter;

    public SearchResultsDisplayer(final SearchResultsFragment searchResultsFragment,
                                  final MarkupsFactory markupsFactory,
                                  final SearchResultsSorter searchResultsSorter) {
        this.searchResultsFragment = searchResultsFragment;
        this.markupsFactory = markupsFactory;
        this.searchResultsSorter = searchResultsSorter;
    }

    public void displaySearchResults(final Set<PreferenceMatch> preferenceMatches) {
        new PreferenceMatchesHighlighter(markupsFactory).highlight(preferenceMatches);
        searchResultsFragment.setSearchResults(
                searchResultsSorter.sort(getPreferences(preferenceMatches)));
    }

    public SearchResultsFragment getSearchResultsFragment() {
        return searchResultsFragment;
    }

    private static Set<SearchablePreference> getPreferences(final Set<PreferenceMatch> preferenceMatches) {
        return preferenceMatches
                .stream()
                .map(PreferenceMatch::preference)
                .collect(Collectors.toSet());
    }
}
