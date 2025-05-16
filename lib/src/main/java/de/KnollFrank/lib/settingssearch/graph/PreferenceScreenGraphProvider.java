package de.KnollFrank.lib.settingssearch.graph;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;

class PreferenceScreenGraphProvider {

    private final PreferenceScreenGraphListener preferenceScreenGraphListener;
    private final ConnectedPreferenceScreenByPreferenceProvider connectedPreferenceScreenByPreferenceProvider;

    public PreferenceScreenGraphProvider(final PreferenceScreenGraphListener preferenceScreenGraphListener,
                                         final ConnectedPreferenceScreenByPreferenceProvider connectedPreferenceScreenByPreferenceProvider) {
        this.preferenceScreenGraphListener = preferenceScreenGraphListener;
        this.connectedPreferenceScreenByPreferenceProvider = connectedPreferenceScreenByPreferenceProvider;
    }

    public Graph<PreferenceScreenWithHost, PreferenceEdge> getPreferenceScreenGraph(final PreferenceScreenWithHost root) {
        final var preferenceScreenGraph = PreferenceScreenGraphFactory.createEmptyPreferenceScreenGraph(preferenceScreenGraphListener);
        buildPreferenceScreenGraph(root, preferenceScreenGraph);
        return preferenceScreenGraph;
    }

    private void buildPreferenceScreenGraph(final PreferenceScreenWithHost root,
                                            final Graph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph) {
        if (preferenceScreenGraph.containsVertex(root)) {
            return;
        }
        preferenceScreenGraph.addVertex(root);
        // FK-TODO: remove sleep
//        try {
//            Thread.sleep(1000);
//        } catch (final InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        connectedPreferenceScreenByPreferenceProvider
                .getConnectedPreferenceScreenByPreference(root)
                .forEach(
                        (preference, child) -> {
                            buildPreferenceScreenGraph(child, preferenceScreenGraph);
                            preferenceScreenGraph.addVertex(child);
                            preferenceScreenGraph.addEdge(root, child, new PreferenceEdge(preference));
                        });
    }
}
