package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import androidx.preference.PreferenceFragmentCompat;

public class DefaultPreferenceFragmentIdProvider implements PreferenceFragmentIdProvider {

    @Override
    public String getId(final PreferenceFragmentCompat preferenceFragment) {
        // FK-TODO: append arguments of preferenceFragment as a string and then use DefaultPreferenceFragmentIdProvider instead of some custom implementations of PreferenceFragmentIdProvider.
        return preferenceFragment.getClass().getName();
    }
}
