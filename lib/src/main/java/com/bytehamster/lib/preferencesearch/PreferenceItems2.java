package com.bytehamster.lib.preferencesearch;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class PreferenceItems2 {

    public static List<PreferenceItem> getPreferenceItems(final List<Preference> preferences,
                                                          final Class<? extends PreferenceFragmentCompat> resId) {
        return preferences
                .stream()
                .map(preference -> getPreferenceItem(preference, resId))
                .collect(Collectors.toList());
    }

    private static PreferenceItem getPreferenceItem(final Preference preference,
                                                    final Class<? extends PreferenceFragmentCompat> resId) {
        final PreferenceItem preferenceItem =
                new PreferenceItem(
                        preference.getTitle() != null ? preference.getTitle().toString() : null,
                        preference.getSummary() != null ? preference.getSummary().toString() : null,
                        preference.getKey(),
                        null,
                        null,
                        resId);
        if (preference instanceof ListPreference) {
            final ListPreference listPreference = ((ListPreference) preference);
            if (listPreference.getEntries() != null) {
                preferenceItem.entries = Arrays.toString(listPreference.getEntries());
            }
        }
        return preferenceItem;
    }
}
