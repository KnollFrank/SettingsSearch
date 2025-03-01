package de.KnollFrank.lib.settingssearch;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

// FK-TODO: use the fact that ConnectedFragmentProvider is the inverse of Fragment2PreferenceFragmentConverter
@FunctionalInterface
public interface ConnectedFragmentProvider {

    Optional<Class<? extends Fragment>> getConnectedFragment(Class<? extends PreferenceFragmentCompat> preferenceFragment);
}
