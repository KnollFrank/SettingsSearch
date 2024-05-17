package com.bytehamster.lib.preferencesearch;

import androidx.preference.Preference;

import java.util.List;
import java.util.stream.Collectors;

public class PreferenceItemFilter {

    public static List<Preference> getSearchablePreferences(final List<Preference> preferences) {
        return preferences
                .stream()
                .filter(preference -> !(preference instanceof SearchPreference))
                .collect(Collectors.toList());
    }
}
