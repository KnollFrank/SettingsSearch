package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import androidx.fragment.app.Fragment;

@FunctionalInterface
public interface PreferenceFragmentIdProvider {

    String getId(Fragment preferenceFragment);
}
