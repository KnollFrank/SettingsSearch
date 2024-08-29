package de.KnollFrank.lib.settingssearch;

import androidx.preference.PreferenceFragmentCompat;

import org.jgrapht.Graph;

public class PreferenceScreensProvider {

    private final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider;

    public PreferenceScreensProvider(final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider) {
        this.preferenceScreenWithHostProvider = preferenceScreenWithHostProvider;
    }

    public ConnectedPreferenceScreens getConnectedPreferenceScreens(final PreferenceFragmentCompat root) {
        final Graph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph =
                getPreferenceScreenGraph(root);
        return new ConnectedPreferenceScreens(
                preferenceScreenGraph,
                preferenceScreenGraph.vertexSet(),
                PreferencePathByPreferenceProvider.getPreferencePathByPreference(preferenceScreenGraph));
    }

    private Graph<PreferenceScreenWithHost, PreferenceEdge> getPreferenceScreenGraph(
            final PreferenceFragmentCompat root) {
        return new PreferenceScreenGraphProvider(preferenceScreenWithHostProvider)
                .getPreferenceScreenGraph(
                        PreferenceScreenWithHost.fromPreferenceFragment(root));
    }
}
