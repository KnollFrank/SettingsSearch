package de.KnollFrank.lib.preferencesearch.provider;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

@FunctionalInterface
public interface PreferenceDialogProvider {

    Optional<Fragment> getPreferenceDialog(Class<? extends PreferenceFragmentCompat> hostOfPreference, Preference preference);
}
