package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

public record FragmentWithPreferenceFragmentConnection<F extends Fragment, P extends PreferenceFragmentCompat & InitializePreferenceFragmentWithFragmentBeforeOnCreate<F>>(
        Class<F> fragment,
        Class<P> preferenceFragment) {
}
