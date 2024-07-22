package de.KnollFrank.lib.preferencesearch.provider;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public interface DialogFragmentByPreference {

    boolean hasDialogFragment(Class<? extends PreferenceFragmentCompat> host, Preference preference);

    DialogFragment getDialogFragment(Class<? extends PreferenceFragmentCompat> host, Preference preference, FragmentManager fragmentManager);
}
