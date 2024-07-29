package de.KnollFrank.lib.preferencesearch;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Collections;

public class PreferenceScreensProvider {

    private final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider;

    public PreferenceScreensProvider(final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider) {
        this.preferenceScreenWithHostProvider = preferenceScreenWithHostProvider;
    }

    public ConnectedPreferenceScreens getConnectedPreferenceScreens(final PreferenceFragmentCompat root) {
        return new ConnectedPreferenceScreens(
                new PreferenceScreenGraphProvider(preferenceScreenWithHostProvider).getPreferenceScreenGraph(PreferenceScreenWithHost.fromPreferenceFragment(root)).vertexSet(),
                Collections.emptyMap());
    }
}
