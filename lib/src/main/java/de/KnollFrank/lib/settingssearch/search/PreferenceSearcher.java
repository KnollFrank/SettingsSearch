package de.KnollFrank.lib.settingssearch.search;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Lists;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

class PreferenceSearcher {

    private final Set<SearchablePreferencePOJO> preferences;

    // FK-TODO: Set<SearchablePreferencePOJO> in einer SQL-Datenkbank speichern.
    public PreferenceSearcher(final Set<SearchablePreferencePOJO> preferences) {
        this.preferences = preferences;
    }

    // FK-TODO: suche nicht mehr in this.preferences, sondern in einer SQL-Datenbank nach der needle.
    public List<PreferenceMatch> searchFor(final String needle) {
        return Lists.concat(
                preferences
                        .stream()
                        .map(searchablePreference -> PreferenceMatcher.getPreferenceMatches(searchablePreference, needle))
                        .collect(Collectors.toList()));
    }
}
