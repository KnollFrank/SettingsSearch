package de.KnollFrank.lib.preferencesearch.results;

import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;

import java.util.List;

class PreferencePreparer {

    public static void preparePreferences(final List<Preference> preferences) {
        preferences.forEach(PreferencePreparer::preparePreference);
    }

    private static void preparePreference(final Preference preference) {
        preference.setEnabled(false);
        preference.setShouldDisableView(false);
        removePreferenceFromItsParent(preference);
    }

    private static void removePreferenceFromItsParent(final Preference preference) {
        final PreferenceGroup parent = preference.getParent();
        if (parent != null) {
            parent.removePreference(preference);
        }
    }
}
