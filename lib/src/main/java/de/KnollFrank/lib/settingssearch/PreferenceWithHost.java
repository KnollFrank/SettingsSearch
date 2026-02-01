package de.KnollFrank.lib.settingssearch;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public record PreferenceWithHost(Preference preference, PreferenceFragmentCompat hostOfPreference) {
}
