package de.KnollFrank.lib.settingssearch;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

// FK-TODO: replace with PreferenceOfHostOfActivity?
public record PreferenceOfHost(Preference preference, PreferenceFragmentCompat hostOfPreference) {
}
