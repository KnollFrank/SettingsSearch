package de.KnollFrank.lib.settingssearch.graph;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;

class PreferenceScreenGraphProvider {

    private final PreferenceScreenGraphListener preferenceScreenGraphListener;
    private final ConnectedPreferenceScreenByPreferenceProvider connectedPreferenceScreenByPreferenceProvider;
    private final AddEdgeToGraphPredicate addEdgeToGraphPredicate;

    public PreferenceScreenGraphProvider(final PreferenceScreenGraphListener preferenceScreenGraphListener,
                                         final ConnectedPreferenceScreenByPreferenceProvider connectedPreferenceScreenByPreferenceProvider,
                                         final AddEdgeToGraphPredicate addEdgeToGraphPredicate) {
        this.preferenceScreenGraphListener = preferenceScreenGraphListener;
        this.connectedPreferenceScreenByPreferenceProvider = connectedPreferenceScreenByPreferenceProvider;
        this.addEdgeToGraphPredicate = addEdgeToGraphPredicate;
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
        connectedPreferenceScreenByPreferenceProvider
                .getConnectedPreferenceScreenByPreference(root)
                // FK-TODO: addEdgeToGraphPredicate hier als filter verwenden
                .forEach(
                        (preference, child) -> {
                            if (addEdgeToGraphPredicate.shallAddEdgeToGraph(root, child, new PreferenceEdge(preference))) {
                                buildPreferenceScreenGraph(child, preferenceScreenGraph);
                                preferenceScreenGraph.addVertex(child);
                                preferenceScreenGraph.addEdge(root, child, new PreferenceEdge(preference));
                            }
                        });
    }
}
