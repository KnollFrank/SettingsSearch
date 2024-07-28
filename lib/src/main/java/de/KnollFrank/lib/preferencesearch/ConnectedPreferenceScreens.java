package de.KnollFrank.lib.preferencesearch;

import java.util.Set;

public class ConnectedPreferenceScreens {

    public final Set<PreferenceScreenWithHost> connectedPreferenceScreens;

    public ConnectedPreferenceScreens(final Set<PreferenceScreenWithHost> connectedPreferenceScreens) {
        this.connectedPreferenceScreens = connectedPreferenceScreens;
    }
}
