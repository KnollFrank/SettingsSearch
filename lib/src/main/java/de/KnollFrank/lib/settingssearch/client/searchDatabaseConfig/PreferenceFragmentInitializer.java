package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

@FunctionalInterface
public interface PreferenceFragmentInitializer<P extends PreferenceFragmentCompat, F extends Fragment> {

    void initializePreferenceFragmentWithFragment(P preferenceFragment, F fragment);
}
