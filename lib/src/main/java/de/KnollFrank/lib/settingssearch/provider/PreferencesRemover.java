package de.KnollFrank.lib.settingssearch.provider;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceGroup;

import java.util.Collection;
import java.util.function.BiPredicate;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.common.Preferences;

class PreferencesRemover {

    public static void removePreferences(
            final Collection<PreferenceScreenWithHost> preferenceScreenWithHostCollection,
            final BiPredicate<Preference, PreferenceFragmentCompat> predicate) {
        for (final PreferenceScreenWithHost preferenceScreenWithHost : preferenceScreenWithHostCollection) {
            removePreferences(preferenceScreenWithHost, predicate);
        }
    }

    private static void removePreferences(
            final PreferenceScreenWithHost preferenceScreenWithHost,
            final BiPredicate<Preference, PreferenceFragmentCompat> predicate) {
        Preferences
                .getAllChildren(preferenceScreenWithHost.preferenceScreen)
                .stream()
                .filter(preference -> predicate.test(preference, preferenceScreenWithHost.host))
                .forEach(PreferencesRemover::removePreference);
    }

    private static void removePreference(final Preference preference) {
        final PreferenceGroup parent = preference.getParent();
        if (parent != null) {
            parent.removePreference(preference);
        }
    }
}
