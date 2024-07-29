package de.KnollFrank.lib.preferencesearch;

import androidx.preference.PreferenceFragmentCompat;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.util.Collections;

public class PreferenceScreensProvider {

    private final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider;

    public PreferenceScreensProvider(final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider) {
        this.preferenceScreenWithHostProvider = preferenceScreenWithHostProvider;
    }

    public ConnectedPreferenceScreens getConnectedPreferenceScreens(final PreferenceFragmentCompat root) {
        return new ConnectedPreferenceScreens(
                getPreferenceScreenGraph(root).vertexSet(),
                Collections.emptyMap());
    }

    private Graph<PreferenceScreenWithHost, DefaultEdge> getPreferenceScreenGraph(final PreferenceFragmentCompat root) {
        return new PreferenceScreenGraphProvider(preferenceScreenWithHostProvider)
                .getPreferenceScreenGraph(
                        PreferenceScreenWithHost.fromPreferenceFragment(root));
    }
}
