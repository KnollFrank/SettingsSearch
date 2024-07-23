package de.KnollFrank.lib.preferencesearch.provider;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public interface PreferenceDialogProvider {

    boolean hasPreferenceDialog(Class<? extends PreferenceFragmentCompat> hostOfPreference, Preference preference);

    Fragment getPreferenceDialog(Class<? extends PreferenceFragmentCompat> hostOfPreference, Preference preference);
}
