package de.KnollFrank.lib.preferencesearch.search;

import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;

import com.google.common.collect.ImmutableList;

import java.util.List;

import de.KnollFrank.lib.preferencesearch.common.PreferenceGroups;

public class Preferences {

    public static void makePreferencesOfPreferenceScreenVisible(final List<Preference> preferences,
                                                                final PreferenceScreen preferenceScreen) {
        makeWholePreferenceScreenInvisible(preferenceScreen);
        makePreferencesAndTheirParentsVisible(preferences);
    }

    private static void makeWholePreferenceScreenInvisible(final PreferenceScreen preferenceScreen) {
        Preferences
                .getAllPreferences(preferenceScreen)
                .forEach(preference -> preference.setVisible(false));
    }

    private static List<Preference> getAllPreferences(final PreferenceScreen preferenceScreen) {
        return ImmutableList
                .<Preference>builder()
                .add(preferenceScreen)
                .addAll(PreferenceGroups.getAllChildren(preferenceScreen))
                .build();
    }

    private static void makePreferencesAndTheirParentsVisible(final List<Preference> preferences) {
        preferences.forEach(Preferences::makePreferenceAndItsParentsVisible);
    }

    private static void makePreferenceAndItsParentsVisible(final Preference preference) {
        if (preference != null) {
            preference.setVisible(true);
            makePreferenceAndItsParentsVisible(preference.getParent());
        }
    }
}
