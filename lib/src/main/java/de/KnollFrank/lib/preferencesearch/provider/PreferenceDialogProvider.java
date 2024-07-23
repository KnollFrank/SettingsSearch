package de.KnollFrank.lib.preferencesearch.provider;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public interface PreferenceDialogProvider {

    boolean hasPreferenceDialog(Class<? extends PreferenceFragmentCompat> host, Preference preference);

    Fragment getPreferenceDialog(Class<? extends PreferenceFragmentCompat> host, Preference preference, FragmentManager fragmentManager);
}
