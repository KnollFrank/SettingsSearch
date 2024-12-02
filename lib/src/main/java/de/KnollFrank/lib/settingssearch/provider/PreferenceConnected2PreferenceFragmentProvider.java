package de.KnollFrank.lib.settingssearch.provider;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

@FunctionalInterface
public interface PreferenceConnected2PreferenceFragmentProvider {

    Optional<Class<? extends Fragment>> getClassOfConnectedPreferenceFragment(Preference preference, final PreferenceFragmentCompat hostOfPreference);
}
