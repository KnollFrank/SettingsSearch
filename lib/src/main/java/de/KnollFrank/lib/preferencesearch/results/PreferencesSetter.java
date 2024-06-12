package de.KnollFrank.lib.preferencesearch.results;

import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;

import java.util.List;

class PreferencesSetter {

    public static void setPreferencesOnPreferenceScreen(final List<Preference> preferences,
                                                        final PreferenceScreen preferenceScreen) {
        preferenceScreen.removeAll();
        addPreferences2PreferenceScreen(preferences, preferenceScreen);
    }

    public static void addPreferences2PreferenceScreen(final List<Preference> preferences,
                                                       final PreferenceScreen preferenceScreen) {
        preferences.forEach(preferenceScreen::addPreference);
    }
}
