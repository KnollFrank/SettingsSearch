package com.bytehamster.lib.preferencesearch;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.List;
import java.util.Set;

class PreferenceItems {

    public static List<PreferenceItem> getPreferenceItems(final Set<Class<? extends PreferenceFragmentCompat>> preferenceFragments,
                                                          final PreferenceProvider preferenceProvider) {
        return new PreferenceItems1(preferenceProvider).getPreferenceItems(preferenceFragments);
    }

    public static List<PreferenceItem> getPreferenceItems(final List<Preference> preferences,
                                                          final Class<? extends PreferenceFragmentCompat> resId) {
        return PreferenceItems2.getPreferenceItems(preferences, resId);
    }
}
