package de.KnollFrank.lib.settingssearch.provider;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

@FunctionalInterface
public interface PreferenceFragmentConnected2PreferenceProvider {

    Optional<Class<? extends PreferenceFragmentCompat>> getPreferenceFragmentConnected2Preference(Preference preference, final PreferenceFragmentCompat hostOfPreference);
}
