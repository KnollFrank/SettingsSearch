package de.KnollFrank.lib.settingssearch.results;

import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;

import de.KnollFrank.lib.settingssearch.common.Preferences;

class PreferenceScreenForSearchPreparer {

    public static void preparePreferenceScreenForSearch(final PreferenceScreen preferenceScreen) {
        Preferences
                .getAllPreferences(preferenceScreen)
                .forEach(PreferenceScreenForSearchPreparer::preparePreferenceForSearch);
    }

    private static void preparePreferenceForSearch(final Preference preference) {
        preference.setEnabled(false);
        preference.setShouldDisableView(false);
    }
}
