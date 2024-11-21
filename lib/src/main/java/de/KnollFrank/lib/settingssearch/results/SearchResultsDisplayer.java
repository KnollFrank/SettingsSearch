package de.KnollFrank.lib.settingssearch.results;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.results.recyclerview.SearchResultsFragment;
import de.KnollFrank.lib.settingssearch.search.PreferenceMatch;
import de.KnollFrank.lib.settingssearch.search.PreferenceMatchesHighlighter;

public class SearchResultsDisplayer {

    // FK-TODO: remove
    private SearchResultsDescription searchResultsDescription;
    private final Supplier<List<Object>> markupsFactory;
    private final SearchResultsFragment searchResultsFragment;
    // FK-TODO: remove
    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    protected SearchResultsDisplayer(final SearchResultsFragment searchResultsFragment,
                                     final Supplier<List<Object>> markupsFactory,
                                     final SearchResultsDescription searchResultsDescription) {
        this.searchResultsFragment = searchResultsFragment;
        this.markupsFactory = markupsFactory;
        this.searchResultsDescription = searchResultsDescription;
    }

    public SearchResultsDescription displaySearchResults(final List<PreferenceMatch> preferenceMatches) {
        final List<SearchablePreferencePOJO> preferences = getPreferences(preferenceMatches);
        searchResultsFragment.setSearchResults(preferences);
        new PreferenceMatchesHighlighter(markupsFactory).highlight(preferenceMatches);
        return searchResultsDescription;
    }

    public SearchResultsDescription getSearchResultsDescription() {
        return searchResultsDescription;
    }

    public SearchResultsFragment getSearchResultsFragment() {
        return searchResultsFragment;
    }

    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(final PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    private static List<SearchablePreferencePOJO> getPreferences(final List<PreferenceMatch> preferenceMatches) {
        return preferenceMatches
                .stream()
                .map(PreferenceMatch::preference)
                .distinct()
                .collect(Collectors.toList());
    }
}
