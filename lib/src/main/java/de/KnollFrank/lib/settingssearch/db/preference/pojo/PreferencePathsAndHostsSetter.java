package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Map;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.common.PreferencePOJOs;

class PreferencePathsAndHostsSetter {

    public static void setPreferencePathsAndHosts(
            final Set<SearchablePreferencePOJO> preferences,
            final Map<SearchablePreferencePOJO, PreferencePath> preferencePathByPreference,
            final Map<SearchablePreferencePOJO, Class<? extends PreferenceFragmentCompat>> hostByPreference) {
        setPreferencePaths(preferences, preferencePathByPreference);
        setHosts(preferences, hostByPreference);
    }

    private static void setPreferencePaths(final Set<SearchablePreferencePOJO> preferences, final Map<SearchablePreferencePOJO, PreferencePath> preferencePathByPreference) {
        preferences.forEach(preference -> setPreferencePathIncludingChildren(preference, preferencePathByPreference));
    }

    private static void setPreferencePathIncludingChildren(final SearchablePreferencePOJO preference,
                                                           final Map<SearchablePreferencePOJO, PreferencePath> preferencePathByPreference) {
        PreferencePOJOs
                .getPreferencesRecursively(preference)
                .forEach(
                        _preference ->
                                _preference.setPreferencePath(preferencePathByPreference.get(_preference)));
    }

    private static void setHosts(final Set<SearchablePreferencePOJO> preferences, final Map<SearchablePreferencePOJO, Class<? extends PreferenceFragmentCompat>> hostByPreference) {
        preferences.forEach(preference -> setHostIncludingChildren(preference, hostByPreference));
    }

    private static void setHostIncludingChildren(final SearchablePreferencePOJO preference,
                                                 final Map<SearchablePreferencePOJO, Class<? extends PreferenceFragmentCompat>> hostByPreference) {
        PreferencePOJOs
                .getPreferencesRecursively(preference)
                .forEach(
                        _preference ->
                                _preference.setHost(hostByPreference.get(_preference)));
    }
}
