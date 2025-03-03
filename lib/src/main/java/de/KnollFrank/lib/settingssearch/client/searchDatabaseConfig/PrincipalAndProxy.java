package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

public record PrincipalAndProxy<Principal extends Fragment, Proxy extends PreferenceFragmentCompat & InitializePreferenceFragmentWithFragmentBeforeOnCreate<Principal>>(
        Class<Principal> principal,
        Class<Proxy> proxy) {
}
