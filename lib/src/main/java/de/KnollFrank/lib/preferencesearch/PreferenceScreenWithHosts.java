package de.KnollFrank.lib.preferencesearch;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import java.util.List;
import java.util.Optional;

public class PreferenceScreenWithHosts {

    public final PreferenceScreen preferenceScreen;
    public final List<PreferenceWithHost> preferenceWithHostList;

    public PreferenceScreenWithHosts(final PreferenceScreen preferenceScreen,
                                     final List<PreferenceWithHost> preferenceWithHostList) {
        this.preferenceScreen = preferenceScreen;
        this.preferenceWithHostList = preferenceWithHostList;
    }

    public Optional<? extends Class<? extends PreferenceFragmentCompat>> findHostByPreference(final Preference preference) {
        return this
                .preferenceWithHostList
                .stream()
                .filter(preferenceWithHost -> preferenceWithHost.preference.equals(preference))
                .map(preferenceWithHost -> preferenceWithHost.host)
                .findFirst();
    }
}
