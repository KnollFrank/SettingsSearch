package de.KnollFrank.lib.settingssearch.search;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.fragment.Fragments;

class PreferenceManagerProvider {

    public static PreferenceManager getPreferenceManager(final Fragments fragments,
                                                         Class<? extends PreferenceFragmentCompat> rootPreferenceFragment) {
        return PreferenceManagerProvider
                .getPreferenceFragment(fragments, rootPreferenceFragment)
                .getPreferenceManager();
    }

    private static PreferenceFragmentCompat getPreferenceFragment(final Fragments fragments,
                                                                  final Class<? extends PreferenceFragmentCompat> rootPreferenceFragment) {
        return (PreferenceFragmentCompat)
                fragments.instantiateAndInitializeFragment(
                        rootPreferenceFragment.getName(),
                        Optional.empty());
    }
}
