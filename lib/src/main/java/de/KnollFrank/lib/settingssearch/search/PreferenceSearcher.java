package de.KnollFrank.lib.settingssearch.search;

import static de.KnollFrank.lib.settingssearch.graph.GraphConverterFactory.createGraphConverter;

import androidx.fragment.app.FragmentActivity;

import java.util.Collections;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Optionals;
import de.KnollFrank.lib.settingssearch.common.graph.ValueTree;
import de.KnollFrank.lib.settingssearch.db.preference.db.SearchablePreferenceScreenGraphRepository;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceOfHostWithinGraph;
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

    private Set<PreferenceMatch> searchFor(final String needle, final Set<SearchablePreferenceOfHostWithinGraph> haystack) {
        return haystack
                .stream()
                .map(searchablePreference -> preferenceMatcher.getPreferenceMatch(searchablePreference, needle))
                .flatMap(Optionals::streamOfPresentElements)
                .collect(Collectors.toSet());
    }

    private Set<SearchablePreferenceOfHostWithinGraph> getHaystack(final Locale locale) {
        return this
                .getPreferences(graphRepository.findGraphById(locale, null, activityContext))
                .stream()
                .filter(searchablePreferenceWithinGraph -> searchablePreferenceWithinGraph.searchablePreference().isVisible())
                .filter(searchResultsFilter::includePreferenceInSearchResults)
                .collect(Collectors.toSet());
    }

    private Set<SearchablePreferenceOfHostWithinGraph> getPreferences(final Optional<SearchablePreferenceScreenGraph> graph) {
        return graph
                .map(SearchablePreferenceScreenGraph::tree)
                .map(ValueTree::graph)
                .map(createGraphConverter()::toJGraphT)
                .map(PojoGraphs::getPreferences)
                .orElseGet(Collections::emptySet);
    }
}
