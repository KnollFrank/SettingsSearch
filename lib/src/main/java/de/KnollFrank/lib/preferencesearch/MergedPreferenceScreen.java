package de.KnollFrank.lib.preferencesearch;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import java.util.List;
import java.util.Optional;

public class MergedPreferenceScreen {

    public final PreferenceScreen preferenceScreen;
    // FK-TODO: replace private final Map<Preference, Class<? extends PreferenceFragmentCompat>> hostByPreference;
    private final List<PreferenceWithHost> preferenceWithHostList;

    public MergedPreferenceScreen(final PreferenceScreen preferenceScreen,
                                  final List<PreferenceWithHost> preferenceWithHostList) {
        this.preferenceScreen = preferenceScreen;
        this.preferenceWithHostList = preferenceWithHostList;
    }

    public Optional<? extends Class<? extends PreferenceFragmentCompat>> findHostByPreference(final Preference preference) {
        return preferenceWithHostList
                .stream()
                .filter(preferenceWithHost -> preferenceWithHost.preference.equals(preference))
                .map(preferenceWithHost -> preferenceWithHost.host)
                .findFirst();
    }
}
