package de.KnollFrank.lib.settingssearch.results;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.results.recyclerview.SearchResultsFragment;
import de.KnollFrank.lib.settingssearch.search.PreferenceMatch;
import de.KnollFrank.lib.settingssearch.search.PreferenceMatchesHighlighter;

public class SearchResultsDisplayer {

    private final Supplier<List<Object>> markupsFactory;
    private final SearchResultsFragment searchResultsFragment;

    protected SearchResultsDisplayer(final SearchResultsFragment searchResultsFragment,
                                     final Supplier<List<Object>> markupsFactory) {
        this.searchResultsFragment = searchResultsFragment;
        this.markupsFactory = markupsFactory;
    }

    public void displaySearchResults(final Set<PreferenceMatch> preferenceMatches) {
        searchResultsFragment.setSearchResults(new ArrayList<>(getPreferences(preferenceMatches)));
        new PreferenceMatchesHighlighter(markupsFactory).highlight(preferenceMatches);
    }

    public SearchResultsFragment getSearchResultsFragment() {
        return searchResultsFragment;
    }

    private static Set<SearchablePreferencePOJO> getPreferences(final Set<PreferenceMatch> preferenceMatches) {
        return preferenceMatches
                .stream()
                .map(PreferenceMatch::preference)
                .collect(Collectors.toSet());
    }
}
