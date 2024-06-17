package de.KnollFrank.lib.preferencesearch.results;

import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;

import de.KnollFrank.lib.preferencesearch.common.Preferences;

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
