package de.KnollFrank.lib.settingssearch;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

public record PreferenceScreenWithHost(PreferenceScreen preferenceScreen,
                                       PreferenceFragmentCompat host) {
}
