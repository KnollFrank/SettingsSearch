package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.preference.PreferenceFragmentCompat;

// FK-TODO: inline host, remove HostWithArguments
public record HostWithArguments(Class<? extends PreferenceFragmentCompat> host) {

    public static HostWithArguments of(final PreferenceFragmentCompat preferenceFragment) {
        return new HostWithArguments(preferenceFragment.getClass());
    }
}
