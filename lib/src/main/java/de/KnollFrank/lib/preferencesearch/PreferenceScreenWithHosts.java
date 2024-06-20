package de.KnollFrank.lib.preferencesearch;

import androidx.preference.PreferenceScreen;

import java.util.List;

public class PreferenceScreenWithHosts {

    public final PreferenceScreen preferenceScreen;
    public final List<PreferenceWithHost> preferenceWithHostList;

    public PreferenceScreenWithHosts(final PreferenceScreen preferenceScreen,
                                     final List<PreferenceWithHost> preferenceWithHostList) {
        this.preferenceScreen = preferenceScreen;
        this.preferenceWithHostList = preferenceWithHostList;
    }
}
