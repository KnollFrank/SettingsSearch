package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Map;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.PreferencePath;

class PreferencePathsAndHostsSetter {

    public static void setPreferencePathsAndHosts(
            final Set<SearchablePreferencePOJO> preferences,
            final Map<SearchablePreferencePOJO, PreferencePath> preferencePathByPreference,
            final Map<SearchablePreferencePOJO, Class<? extends PreferenceFragmentCompat>> hostByPreference) {
        setPreferencePaths(preferences, preferencePathByPreference);
        setHosts(preferences, hostByPreference);
    }

    private static void setPreferencePaths(final Set<SearchablePreferencePOJO> preferences,
                                           final Map<SearchablePreferencePOJO, PreferencePath> preferencePathByPreference) {
        for (final SearchablePreferencePOJO preference : preferences) {
            preference.setPreferencePath(preferencePathByPreference.get(preference));
        }
    }

    private static void setHosts(final Set<SearchablePreferencePOJO> preferences, final Map<SearchablePreferencePOJO, Class<? extends PreferenceFragmentCompat>> hostByPreference) {
        for (final SearchablePreferencePOJO preference : preferences) {
            preference.setHost(hostByPreference.get(preference));
        }
    }
}
