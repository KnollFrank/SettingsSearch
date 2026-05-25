package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import androidx.fragment.app.Fragment;

// FK-TODO: rename to FragmentIdProvider
@FunctionalInterface
public interface PreferenceFragmentIdProvider {

    String getId(Fragment fragment);
}
