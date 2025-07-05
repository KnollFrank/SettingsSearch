package de.KnollFrank.lib.settingssearch.search;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Optionals;
import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenGraphDAO;
import de.KnollFrank.lib.settingssearch.graph.PojoGraphs;
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
        return graphDAO
                .load()
                .map(PojoGraphs::getPreferences)
                .orElseGet(Collections::emptySet)
                .stream()
                .filter(includePreferenceInSearchResultsPredicate::includePreferenceInSearchResults)
                .map(searchablePreference -> PreferenceMatcher.getPreferenceMatch(searchablePreference, needle))
                .flatMap(Optionals::streamOfPresentElements)
                .collect(Collectors.toSet());
    }
}
