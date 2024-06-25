package de.KnollFrank.lib.preferencesearch.provider;

import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;

import de.KnollFrank.lib.preferencesearch.common.Preferences;

class PreferencesRemover {

    public static void removeInvisiblePreferences(final PreferenceScreen preferenceScreen) {
        Preferences
                .getAllPreferences(preferenceScreen)
                .stream()
                .filter(preference -> !preference.isVisible())
                .forEach(preference -> removePreferenceFromPreferenceScreen(preference, preferenceScreen));
    }

    private static void removePreferenceFromPreferenceScreen(final Preference preference, final PreferenceScreen preferenceScreen) {
        if (preference.hasKey()) {
            preferenceScreen.removePreferenceRecursively(preference.getKey());
        }
    }
}
