package de.KnollFrank.lib.settingssearch.search;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Optionals;
import de.KnollFrank.lib.settingssearch.common.PreferencePOJOs;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.provider.IncludePreferenceInSearchResultsPredicate;

class PreferenceSearcher {

    // FK-TODO: SQL-Datenbank verwenden? (siehe Branch precompute-MergedPreferenceScreen-SQLite)
    private final Set<SearchablePreferencePOJO> preferences;
    private final IncludePreferenceInSearchResultsPredicate includePreferenceInSearchResultsPredicate;
    private final Map<SearchablePreferencePOJO, Class<? extends PreferenceFragmentCompat>> hostByPreference;

    public PreferenceSearcher(final Set<SearchablePreferencePOJO> preferences,
                              final IncludePreferenceInSearchResultsPredicate includePreferenceInSearchResultsPredicate,
                              final Map<SearchablePreferencePOJO, Class<? extends PreferenceFragmentCompat>> hostByPreference) {
        this.preferences = preferences;
        this.includePreferenceInSearchResultsPredicate = includePreferenceInSearchResultsPredicate;
        this.hostByPreference = hostByPreference;
    }

    public Set<PreferenceMatch> searchFor(final String needle) {
        return PreferencePOJOs
                .getPreferencesRecursively(preferences)
                .stream()
                .filter(this::includePreferenceInSearchResults)
                .map(searchablePreference -> PreferenceMatcher.getPreferenceMatch(searchablePreference, needle))
                .flatMap(Optionals::stream)
                .collect(Collectors.toSet());
    }

    private boolean includePreferenceInSearchResults(final SearchablePreferencePOJO preference) {
        return includePreferenceInSearchResultsPredicate.includePreferenceInSearchResults(
                preference,
                hostByPreference.get(preference));
    }
}
