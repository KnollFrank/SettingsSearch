package de.KnollFrank.lib.preferencesearch.provider;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public interface FragmentByPreference {

    boolean hasFragment(Class<? extends PreferenceFragmentCompat> host, Preference preference);

    Fragment getFragment(Class<? extends PreferenceFragmentCompat> host, Preference preference, FragmentManager fragmentManager);
}
