package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import java.util.Map;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.common.SearchablePreferences;

public class PreferencePathsSetter {

    private final Map<SearchablePreference, PreferencePath> preferencePathByPreference;

    public PreferencePathsSetter(final Map<SearchablePreference, PreferencePath> preferencePathByPreference) {
        this.preferencePathByPreference = preferencePathByPreference;
    }

    public void setPreferencePaths(final Set<SearchablePreference> preferences) {
        preferences.forEach(this::setPreferencePathIncludingChildren);
    }

    private void setPreferencePathIncludingChildren(final SearchablePreference preference) {
        SearchablePreferences
                .getPreferencesRecursively(preference)
                .forEach(
                        _preference ->
                                _preference.setPreferencePath(preferencePathByPreference.get(_preference)));
    }
}
