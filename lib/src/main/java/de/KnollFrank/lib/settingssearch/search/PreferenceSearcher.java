package de.KnollFrank.lib.settingssearch.search;

import org.jgrapht.Graph;

import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Optionals;
import de.KnollFrank.lib.settingssearch.common.Sets;
import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenGraphDAO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.provider.IncludePreferenceInSearchResultsPredicate;

class PreferenceSearcher {

    private final SearchablePreferenceScreenGraphDAO graphDAO;
    private final IncludePreferenceInSearchResultsPredicate includePreferenceInSearchResultsPredicate;

    public PreferenceSearcher(final SearchablePreferenceScreenGraphDAO graphDAO,
                              final IncludePreferenceInSearchResultsPredicate includePreferenceInSearchResultsPredicate) {
        this.graphDAO = graphDAO;
        this.includePreferenceInSearchResultsPredicate = includePreferenceInSearchResultsPredicate;
    }

    public Set<PreferenceMatch> searchFor(final String needle) {
        return PreferenceSearcher
                .getSearchablePreferences(graphDAO.load())
                .stream()
                .filter(includePreferenceInSearchResultsPredicate::includePreferenceInSearchResults)
                .map(searchablePreference -> PreferenceMatcher.getPreferenceMatch(searchablePreference, needle))
                .flatMap(Optionals::streamOfPresentElements)
                .collect(Collectors.toSet());
    }

    private static Set<SearchablePreference> getSearchablePreferences(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph) {
        return Sets.union(
                graph
                        .vertexSet()
                        .stream()
                        .map(SearchablePreferenceScreen::allPreferences)
                        .collect(Collectors.toSet()));
    }
}
