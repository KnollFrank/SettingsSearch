package de.KnollFrank.lib.settingssearch.search;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Lists;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

class PreferenceSearcher {

    // FK-TODO: SQL-Datenbank verwenden, siehe Branch precompute-MergedPreferenceScreen-SQLite
    private final Set<SearchablePreferencePOJO> preferences;

    public PreferenceSearcher(final Set<SearchablePreferencePOJO> preferences) {
        this.preferences = preferences;
    }

    public List<PreferenceMatch> searchFor(final String needle) {
        return Lists.concat(
                preferences
                        .stream()
                        .map(searchablePreference -> PreferenceMatcher.getPreferenceMatches(searchablePreference, needle))
                        .collect(Collectors.toList()));
    }
}
