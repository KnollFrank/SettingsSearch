package de.KnollFrank.lib.settingssearch;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class PreferenceWithHost {

    public final Preference preference;
    public final PreferenceFragmentCompat host;

    public PreferenceWithHost(final Preference preference, final PreferenceFragmentCompat host) {
        this.preference = preference;
        this.host = host;
    }
}
