package de.KnollFrank.lib.settingssearch.provider;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

@FunctionalInterface
public interface PreferenceConnected2PreferenceFragmentProvider {

    // FK-TODO: use Optional<Class<? extends Fragment>> as return type?
    Optional<String> getClassNameOfConnectedPreferenceFragment(Preference preference, final PreferenceFragmentCompat hostOfPreference);
}
