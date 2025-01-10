package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Map;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.common.SearchablePreferences;

public class PreferencePathsAndHostsSetter {

    private final Map<SearchablePreference, PreferencePath> preferencePathByPreference;
    private final Map<SearchablePreference, Class<? extends PreferenceFragmentCompat>> hostByPreference;

    public PreferencePathsAndHostsSetter(final Map<SearchablePreference, PreferencePath> preferencePathByPreference,
                                         final Map<SearchablePreference, Class<? extends PreferenceFragmentCompat>> hostByPreference) {
        this.preferencePathByPreference = preferencePathByPreference;
        this.hostByPreference = hostByPreference;
    }

    public void setPreferencePathsAndHosts(final Set<SearchablePreference> preferences) {
        setPreferencePaths(preferences);
        setHosts(preferences);
    }

    private void setPreferencePaths(final Set<SearchablePreference> preferences) {
        preferences.forEach(this::setPreferencePathIncludingChildren);
    }

    private void setPreferencePathIncludingChildren(final SearchablePreference preference) {
        SearchablePreferences
                .getPreferencesRecursively(preference)
                .forEach(
                        _preference ->
                                _preference.setPreferencePath(preferencePathByPreference.get(_preference)));
    }

    private void setHosts(final Set<SearchablePreference> preferences) {
        preferences.forEach(this::setHostIncludingChildren);
    }

    private void setHostIncludingChildren(final SearchablePreference preference) {
        SearchablePreferences
                .getPreferencesRecursively(preference)
                .forEach(
                        _preference ->
                                _preference.setHost(hostByPreference.get(_preference)));
    }
}
