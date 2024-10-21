package de.KnollFrank.lib.settingssearch.search;

import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;

import java.util.List;

import de.KnollFrank.lib.settingssearch.common.Preferences;

class PreferenceVisibility {

    public static void makePreferencesOfPreferenceScreenVisible(final List<Preference> preferences,
                                                                final PreferenceScreen preferenceScreen) {
        makeWholePreferenceScreenInvisible(preferenceScreen);
        makePreferencesAndTheirParentsVisible(preferences);
    }

    private static void makeWholePreferenceScreenInvisible(final PreferenceScreen preferenceScreen) {
        Preferences
                .getPreferencesRecursively(preferenceScreen)
                .forEach(preference -> preference.setVisible(false));
    }

    private static void makePreferencesAndTheirParentsVisible(final List<Preference> preferences) {
        preferences.forEach(PreferenceVisibility::makePreferenceAndItsParentsVisible);
    }

    private static void makePreferenceAndItsParentsVisible(final Preference preference) {
        if (preference != null) {
            preference.setVisible(true);
            makePreferenceAndItsParentsVisible(preference.getParent());
        }
    }
}
