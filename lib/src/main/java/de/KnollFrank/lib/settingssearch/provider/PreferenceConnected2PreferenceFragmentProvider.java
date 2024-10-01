package de.KnollFrank.lib.settingssearch.provider;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

@FunctionalInterface
public interface PreferenceConnected2PreferenceFragmentProvider {

    // FK-FIXME: preference ist eine SearchablePreference, sollte aber eine originale Preference sein.
    Optional<String> getClassNameOfConnectedPreferenceFragment(Preference preference, final PreferenceFragmentCompat hostOfPreference);
}
