package de.KnollFrank.lib.settingssearch;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

@FunctionalInterface
public interface ConnectedFragmentProvider {

    Optional<Class<? extends Fragment>> getConnectedFragment(Class<? extends PreferenceFragmentCompat> preferenceFragment);
}
