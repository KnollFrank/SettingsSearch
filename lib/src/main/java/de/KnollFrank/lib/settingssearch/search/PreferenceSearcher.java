package de.KnollFrank.lib.settingssearch.search;

import static de.KnollFrank.lib.settingssearch.search.PreferenceMatcher.getPreferenceMatch;

import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Optionals;
import de.KnollFrank.lib.settingssearch.common.SearchablePreferences;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.provider.IncludePreferenceInSearchResultsPredicate;

class PreferenceSearcher {

    // FK-TODO: SQL-Datenbank verwenden? (siehe Branch precompute-MergedPreferenceScreen-SQLite)
    private final Set<SearchablePreference> preferences;
    private final IncludePreferenceInSearchResultsPredicate includePreferenceInSearchResultsPredicate;

    public PreferenceSearcher(final Set<SearchablePreference> preferences,
                              final IncludePreferenceInSearchResultsPredicate includePreferenceInSearchResultsPredicate) {
        this.preferences = preferences;
        this.includePreferenceInSearchResultsPredicate = includePreferenceInSearchResultsPredicate;
    }

    public Set<PreferenceMatch> searchFor(final String needle) {
        return SearchablePreferences
                .getPreferencesRecursively(preferences)
                .stream()
                .filter(includePreferenceInSearchResultsPredicate::includePreferenceInSearchResults)
                .map(searchablePreference -> getPreferenceMatch(searchablePreference, needle))
                .flatMap(Optionals::streamOfPresentElements)
                .collect(Collectors.toSet());
    }
}
