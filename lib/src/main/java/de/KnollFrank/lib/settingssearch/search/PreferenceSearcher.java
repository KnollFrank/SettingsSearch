package de.KnollFrank.lib.settingssearch.search;

import androidx.preference.PreferenceFragmentCompat;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.provider.ShallIncludePreferenceInSearchResults;

class PreferenceSearcher {

    // FK-TODO: SQL-Datenbank verwenden? (siehe Branch precompute-MergedPreferenceScreen-SQLite)
    private final Set<SearchablePreferencePOJO> preferences;
    private final ShallIncludePreferenceInSearchResults shallIncludePreferenceInSearchResults;
    private final Map<SearchablePreferencePOJO, Class<? extends PreferenceFragmentCompat>> hostByPreference;

    public PreferenceSearcher(final Set<SearchablePreferencePOJO> preferences,
                              final ShallIncludePreferenceInSearchResults shallIncludePreferenceInSearchResults,
                              final Map<SearchablePreferencePOJO, Class<? extends PreferenceFragmentCompat>> hostByPreference) {
        this.preferences = preferences;
        this.shallIncludePreferenceInSearchResults = shallIncludePreferenceInSearchResults;
        this.hostByPreference = hostByPreference;
    }

    public List<PreferenceMatch> searchFor(final String needle) {
        return preferences
                .stream()
                .filter(this::shallIncludePreferenceOfHostInSearchResults)
                .flatMap(searchablePreference -> PreferenceMatcher.getPreferenceMatches(searchablePreference, needle).stream())
                .collect(Collectors.toList());
    }

    private boolean shallIncludePreferenceOfHostInSearchResults(final SearchablePreferencePOJO preference) {
        return shallIncludePreferenceInSearchResults.shallIncludePreferenceOfHostInSearchResults(
                preference,
                hostByPreference.get(preference));
    }
}
