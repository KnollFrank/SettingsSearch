package de.KnollFrank.lib.settingssearch;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

public record PreferenceScreenWithHostClass(PreferenceScreen preferenceScreen,
                                            Class<? extends PreferenceFragmentCompat> host) {
}
