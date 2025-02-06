package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import androidx.fragment.app.Fragment;

public interface InitializePreferenceFragmentWithFragmentBeforeOnCreate<F extends Fragment> {

    void initializePreferenceFragmentWithFragmentBeforeOnCreate(F fragment);
}
