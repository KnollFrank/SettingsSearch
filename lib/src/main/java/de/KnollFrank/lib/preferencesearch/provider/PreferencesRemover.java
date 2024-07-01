package de.KnollFrank.lib.preferencesearch.provider;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import java.util.function.BiPredicate;

import de.KnollFrank.lib.preferencesearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.preferencesearch.common.Preferences;

class PreferencesRemover {

    public static void removeInvisiblePreferences(final PreferenceScreen preferenceScreen) {
        // FK-TODO: use method removePreferences()
        Preferences
                .getAllPreferences(preferenceScreen)
                .stream()
                .filter(preference -> !preference.isVisible())
                .forEach(preference -> removePreferenceFromPreferenceScreen(preference, preferenceScreen));
    }

    public static void removePreferences(final PreferenceScreenWithHost preferenceScreenWithHost,
                                         final BiPredicate<Preference, Class<? extends PreferenceFragmentCompat>> predicate) {
        Preferences
                .getAllPreferences(preferenceScreenWithHost.preferenceScreen)
                .stream()
                .filter(preference -> predicate.test(preference, preferenceScreenWithHost.host))
                .forEach(preference -> removePreferenceFromPreferenceScreen(preference, preferenceScreenWithHost.preferenceScreen));
    }

    private static void removePreferenceFromPreferenceScreen(final Preference preference, final PreferenceScreen preferenceScreen) {
        if (preference.hasKey()) {
            preferenceScreen.removePreferenceRecursively(preference.getKey());
        }
    }
}
