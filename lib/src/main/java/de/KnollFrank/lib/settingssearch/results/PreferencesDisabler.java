package de.KnollFrank.lib.settingssearch.results;

import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;

import java.util.List;

import de.KnollFrank.lib.settingssearch.common.Preferences;

class PreferencesDisabler {

    public static void disablePreferences(final PreferenceScreen preferenceScreen) {
        disablePreferences(Preferences.getPreferencesRecursively(preferenceScreen));
    }

    private static void disablePreferences(final List<Preference> preferences) {
        preferences.forEach(PreferencesDisabler::disablePreference);
    }

    private static void disablePreference(final Preference preference) {
        preference.setEnabled(false);
        preference.setShouldDisableView(false);
    }
}
