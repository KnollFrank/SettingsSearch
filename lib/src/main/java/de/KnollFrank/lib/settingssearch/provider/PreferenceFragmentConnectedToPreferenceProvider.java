package de.KnollFrank.lib.settingssearch.provider;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

@FunctionalInterface
public interface PreferenceFragmentConnectedToPreferenceProvider {

    Optional<Class<? extends PreferenceFragmentCompat>> getPreferenceFragmentConnectedToPreference(Preference preference, PreferenceFragmentCompat hostOfPreference);
}
