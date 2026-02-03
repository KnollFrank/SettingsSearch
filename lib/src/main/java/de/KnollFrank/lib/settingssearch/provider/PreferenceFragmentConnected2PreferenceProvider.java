package de.KnollFrank.lib.settingssearch.provider;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

@FunctionalInterface
// FK-TODO: replace 2 with To
public interface PreferenceFragmentConnected2PreferenceProvider {

    // FK-TODO: replace 2 with To
    Optional<Class<? extends PreferenceFragmentCompat>> getPreferenceFragmentConnected2Preference(Preference preference, PreferenceFragmentCompat hostOfPreference);
}
