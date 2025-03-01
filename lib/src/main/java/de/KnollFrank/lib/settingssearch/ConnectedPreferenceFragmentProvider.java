package de.KnollFrank.lib.settingssearch;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

@FunctionalInterface
public interface ConnectedPreferenceFragmentProvider {

    Optional<Class<? extends PreferenceFragmentCompat>> getConnectedPreferenceFragment(Class<? extends Fragment> fragment);
}
