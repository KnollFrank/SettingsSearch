package de.KnollFrank.lib.preferencesearch.results;

import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;

import java.util.List;

public class PreferencePreparer {

    public static void preparePreferences(final List<Preference> preferences) {
        preferences.forEach(PreferencePreparer::preparePreference);
    }

    public static void removePreferenceFromItsParent(final Preference preference) {
        final PreferenceGroup parent = preference.getParent();
        if (parent != null) {
            parent.removePreference(preference);
        }
    }

    private static void preparePreference(final Preference preference) {
        preference.setEnabled(false);
        preference.setShouldDisableView(false);
        removePreferenceFromItsParent(preference);
    }
}
