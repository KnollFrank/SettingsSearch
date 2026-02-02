package de.KnollFrank.lib.settingssearch;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

// FK-TODO: add ActivityDescription?
public record PreferenceOfHost(Preference preference, PreferenceFragmentCompat hostOfPreference) {
}
