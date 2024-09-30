package de.KnollFrank.lib.settingssearch;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

public record PreferenceScreenWithHost(PreferenceScreen preferenceScreen,
                                       PreferenceFragmentCompat host) {

    public static PreferenceScreenWithHost fromPreferenceFragment(final PreferenceFragmentCompat preferenceFragment) {
        return new PreferenceScreenWithHost(
                new PreferenceScreenCopier2(preferenceFragment.getPreferenceManager())
                        .copy(preferenceFragment.getPreferenceScreen()),
                preferenceFragment);
    }
}
