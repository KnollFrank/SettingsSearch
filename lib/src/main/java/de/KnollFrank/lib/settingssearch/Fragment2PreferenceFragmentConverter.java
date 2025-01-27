package de.KnollFrank.lib.settingssearch;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

@FunctionalInterface
public interface Fragment2PreferenceFragmentConverter {

    // FK-TODO: rename convert() to asSearchablePreferenceFragment()
    Optional<PreferenceFragmentCompat> convert(Fragment fragment);
}
