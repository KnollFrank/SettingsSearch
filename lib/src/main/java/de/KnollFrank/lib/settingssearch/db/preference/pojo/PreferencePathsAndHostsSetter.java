package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Map;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.common.PreferencePOJOs;

public class PreferencePathsAndHostsSetter {

    private final Map<SearchablePreferencePOJO, PreferencePath> preferencePathByPreference;
    private final Map<SearchablePreferencePOJO, Class<? extends PreferenceFragmentCompat>> hostByPreference;

    public PreferencePathsAndHostsSetter(final Map<SearchablePreferencePOJO, PreferencePath> preferencePathByPreference,
                                         final Map<SearchablePreferencePOJO, Class<? extends PreferenceFragmentCompat>> hostByPreference) {
        this.preferencePathByPreference = preferencePathByPreference;
        this.hostByPreference = hostByPreference;
    }

    public void setPreferencePathsAndHosts(final Set<SearchablePreferencePOJO> preferences) {
        setPreferencePaths(preferences);
        setHosts(preferences);
    }

    private void setPreferencePaths(final Set<SearchablePreferencePOJO> preferences) {
        preferences.forEach(this::setPreferencePathIncludingChildren);
    }

    private void setPreferencePathIncludingChildren(final SearchablePreferencePOJO preference) {
        PreferencePOJOs
                .getPreferencesRecursively(preference)
                .forEach(
                        _preference ->
                                _preference.setPreferencePath(preferencePathByPreference.get(_preference)));
    }

    private void setHosts(final Set<SearchablePreferencePOJO> preferences) {
        preferences.forEach(this::setHostIncludingChildren);
    }

    private void setHostIncludingChildren(final SearchablePreferencePOJO preference) {
        PreferencePOJOs
                .getPreferencesRecursively(preference)
                .forEach(
                        _preference ->
                                _preference.setHost(hostByPreference.get(_preference)));
    }
}
