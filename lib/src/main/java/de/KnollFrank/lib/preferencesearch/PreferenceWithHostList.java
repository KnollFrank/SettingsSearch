package de.KnollFrank.lib.preferencesearch;

import androidx.preference.Preference;

import java.util.List;
import java.util.stream.Collectors;

public class PreferenceWithHostList {

    public static List<Preference> getPreferences(final List<PreferenceWithHost> preferenceWithHostList) {
        return preferenceWithHostList
                .stream()
                .map(preferenceWithHost -> preferenceWithHost.preference)
                .collect(Collectors.toList());
    }
}
