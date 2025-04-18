package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.common.SearchablePreferences;

public class PredecessorSetter {

    private final Map<SearchablePreference, Optional<SearchablePreference>> predecessorByPreference;

    public PredecessorSetter(final Map<SearchablePreference, Optional<SearchablePreference>> predecessorByPreference) {
        this.predecessorByPreference = predecessorByPreference;
    }

    public void setPredecessors(final Set<SearchablePreference> preferences) {
        preferences.forEach(this::setPredecessorIncludingChildren);
    }

    private void setPredecessorIncludingChildren(final SearchablePreference preference) {
        SearchablePreferences
                .getPreferencesRecursively(preference)
                .forEach(_preference -> _preference.setPredecessor(predecessorByPreference.get(_preference)));
    }
}
