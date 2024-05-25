package com.bytehamster.lib.preferencesearch;

import androidx.preference.PreferenceFragmentCompat;

public class PreferenceScreenWithHostFactory {

    public static PreferenceScreenWithHost createPreferenceScreenWithHost(final PreferenceFragmentCompat preferenceFragment) {
        return new PreferenceScreenWithHost(
                preferenceFragment.getPreferenceScreen(),
                preferenceFragment.getClass());
    }
}
