package de.KnollFrank.lib.settingssearch.search;

import androidx.fragment.app.FragmentActivity;

import java.util.Collections;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Optionals;
import de.KnollFrank.lib.settingssearch.db.preference.db.SearchablePreferenceScreenGraphRepository;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceOfHostWithinTree;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraph;
import de.KnollFrank.lib.settingssearch.graph.PojoGraphs;
import de.KnollFrank.lib.settingssearch.results.SearchResultsFilter;

class PreferenceSearcher<C> {

    private final SearchablePreferenceScreenGraphRepository<C> graphRepository;
    private final SearchResultsFilter searchResultsFilter;
    private final PreferenceMatcher preferenceMatcher;
    private final FragmentActivity activityContext;

    public PreferenceSearcher(final SearchablePreferenceScreenGraphRepository<C> graphRepository,
                              final SearchResultsFilter searchResultsFilter,
                              final PreferenceMatcher preferenceMatcher,
                              final FragmentActivity activityContext) {
        this.graphRepository = graphRepository;
        this.searchResultsFilter = searchResultsFilter;
        this.preferenceMatcher = preferenceMatcher;
        this.activityContext = activityContext;
    }

    public Set<PreferenceMatch> searchFor(final String needle, final Locale locale) {
        return searchFor(needle, getHaystack(locale));
    }

    private Set<PreferenceMatch> searchFor(final String needle, final Set<SearchablePreferenceOfHostWithinTree> haystack) {
        return haystack
                .stream()
                .map(searchablePreference -> preferenceMatcher.getPreferenceMatch(searchablePreference, needle))
                .flatMap(Optionals::streamOfPresentElements)
                .collect(Collectors.toSet());
    }

    private Set<SearchablePreferenceOfHostWithinTree> getHaystack(final Locale locale) {
        return this
                .getPreferences(graphRepository.findGraphById(locale, null, activityContext))
                .stream()
                .filter(searchablePreferenceWithinGraph -> searchablePreferenceWithinGraph.searchablePreference().isVisible())
                .filter(searchResultsFilter::includePreferenceInSearchResults)
                .collect(Collectors.toSet());
    }

    private Set<SearchablePreferenceOfHostWithinTree> getPreferences(final Optional<SearchablePreferenceScreenGraph> graph) {
        return graph
                .map(SearchablePreferenceScreenGraph::tree)
                .map(PojoGraphs::getPreferences)
                .orElseGet(Collections::emptySet);
    }
}
