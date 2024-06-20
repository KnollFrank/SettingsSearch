package de.KnollFrank.lib.preferencesearch;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PreferenceWithHostList {

    public static List<Preference> getPreferences(final List<PreferenceWithHost> preferenceWithHostList) {
        return preferenceWithHostList
                .stream()
                .map(preferenceWithHost -> preferenceWithHost.preference)
                .collect(Collectors.toList());
    }

    public static Optional<? extends Class<? extends PreferenceFragmentCompat>> findHostByPreference(
            final List<PreferenceWithHost> preferenceWithHostList,
            final Preference preference) {
        return preferenceWithHostList
                .stream()
                .filter(preferenceWithHost -> preferenceWithHost.preference.equals(preference))
                .map(preferenceWithHost -> preferenceWithHost.host)
                .findFirst();
    }
}
