package de.KnollFrank.lib.settingssearch;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

@FunctionalInterface
public interface PrincipalProvider {

    // FK-TODO: rename parameter to proxy in all implementations
    Optional<Class<? extends Fragment>> getPrincipal(Class<? extends PreferenceFragmentCompat> proxy);
}
