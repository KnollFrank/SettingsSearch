package de.KnollFrank.lib.settingssearch.results;

import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;

import de.KnollFrank.lib.settingssearch.common.Preferences;

// FK-TODO: rename class
class PreferenceScreenForSearchPreparer {

    // FK-TODO: rename method to disablePreferences()
    public static void preparePreferenceScreenForSearch(final PreferenceScreen preferenceScreen) {
        Preferences
                .getPreferencesRecursively(preferenceScreen)
                .forEach(PreferenceScreenForSearchPreparer::preparePreferenceForSearch);
    }

    private static void preparePreferenceForSearch(final Preference preference) {
        preference.setEnabled(false);
        preference.setShouldDisableView(false);
    }
}
