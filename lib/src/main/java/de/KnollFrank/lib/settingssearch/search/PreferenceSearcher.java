package de.KnollFrank.lib.settingssearch.search;

import java.util.Collections;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Optionals;
import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenGraphDAO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraph;
import de.KnollFrank.lib.settingssearch.graph.PojoGraphs;
import de.KnollFrank.lib.settingssearch.results.SearchResultsFilter;

class PreferenceSearcher {

    private final SearchablePreferenceScreenGraphDAO graphDAO;
    private final SearchResultsFilter searchResultsFilter;
    private final PreferenceMatcher preferenceMatcher;

    public PreferenceSearcher(final SearchablePreferenceScreenGraphDAO graphDAO,
                              final SearchResultsFilter searchResultsFilter,
                              final PreferenceMatcher preferenceMatcher) {
        this.graphDAO = graphDAO;
        this.searchResultsFilter = searchResultsFilter;
        this.preferenceMatcher = preferenceMatcher;
    }

    public Set<PreferenceMatch> searchFor(final String needle, final Locale locale) {
        return this
                .getPreferences(graphDAO.findGraphById(locale))
                .stream()
                .filter(SearchablePreference::isVisible)
                .filter(searchResultsFilter::includePreferenceInSearchResults)
                .map(searchablePreference -> preferenceMatcher.getPreferenceMatch(searchablePreference, needle))
                .flatMap(Optionals::streamOfPresentElements)
                .collect(Collectors.toSet());
    }

    private Set<SearchablePreference> getPreferences(final Optional<SearchablePreferenceScreenGraph> graph) {
        return graph
                .map(SearchablePreferenceScreenGraph::graph)
                .map(PojoGraphs::getPreferences)
                .orElseGet(Collections::emptySet);
    }
}
