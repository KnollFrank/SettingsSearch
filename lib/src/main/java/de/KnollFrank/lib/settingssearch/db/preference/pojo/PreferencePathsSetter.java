package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.common.SearchablePreferences;

// FK-TODO: rename to PredecessorSetter
public class PreferencePathsSetter {

    private final Map<SearchablePreference, Optional<SearchablePreference>> predecessorByPreference;

    public PreferencePathsSetter(final Map<SearchablePreference, Optional<SearchablePreference>> predecessorByPreference) {
        this.predecessorByPreference = predecessorByPreference;
    }

    public void setPreferencePaths(final Set<SearchablePreference> preferences) {
        preferences.forEach(this::setPreferencePathIncludingChildren);
    }

    private void setPreferencePathIncludingChildren(final SearchablePreference preference) {
        SearchablePreferences
                .getPreferencesRecursively(preference)
                .forEach(_preference -> _preference.setPredecessor(predecessorByPreference.get(_preference)));
    }
}
