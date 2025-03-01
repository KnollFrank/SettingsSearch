package de.KnollFrank.lib.settingssearch;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

@FunctionalInterface
public interface ProxyProvider {

    // FK-TODO: rename parameter to principal in all implementations
    Optional<Class<? extends PreferenceFragmentCompat>> getProxy(Class<? extends Fragment> principal);
}
