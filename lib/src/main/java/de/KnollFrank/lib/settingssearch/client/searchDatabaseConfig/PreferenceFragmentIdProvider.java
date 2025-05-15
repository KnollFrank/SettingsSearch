package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import androidx.preference.PreferenceFragmentCompat;

@FunctionalInterface
public interface PreferenceFragmentIdProvider {

    String getId(PreferenceFragmentCompat preferenceFragment);
}
