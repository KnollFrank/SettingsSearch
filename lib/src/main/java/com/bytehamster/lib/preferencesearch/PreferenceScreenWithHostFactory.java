package com.bytehamster.lib.preferencesearch;

import androidx.preference.PreferenceFragmentCompat;

public class PreferenceScreenWithHostFactory {

    public static PreferenceScreenWithHost createPreferenceScreenWithHost(final PreferenceFragmentCompat preferenceFragmentCompat) {
        return new PreferenceScreenWithHost(
                preferenceFragmentCompat.getPreferenceScreen(),
                preferenceFragmentCompat.getClass());
    }
}
