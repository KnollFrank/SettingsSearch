package de.KnollFrank.lib.preferencesearch;

import androidx.preference.Preference;

import java.util.Map;
import java.util.Set;

public class ConnectedPreferenceScreens {

    public final Set<PreferenceScreenWithHost> connectedPreferenceScreens;
    public final Map<Preference, PreferencePath> preferencePathByPreference;

    public ConnectedPreferenceScreens(final Set<PreferenceScreenWithHost> connectedPreferenceScreens,
                                      final Map<Preference, PreferencePath> preferencePathByPreference) {
        this.connectedPreferenceScreens = connectedPreferenceScreens;
        this.preferencePathByPreference = preferencePathByPreference;
    }
}
