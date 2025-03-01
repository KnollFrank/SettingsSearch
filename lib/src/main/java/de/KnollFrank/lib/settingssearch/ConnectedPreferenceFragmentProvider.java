package de.KnollFrank.lib.settingssearch;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

// FK-TODO: rename to ProxyProvider
@FunctionalInterface
public interface ConnectedPreferenceFragmentProvider {

    // FK-TODO: rename to getProxy()
    Optional<Class<? extends PreferenceFragmentCompat>> getConnectedPreferenceFragment(Class<? extends Fragment> fragment);
}
