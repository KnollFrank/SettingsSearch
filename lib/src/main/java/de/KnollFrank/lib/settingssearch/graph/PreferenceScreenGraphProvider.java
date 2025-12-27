package de.KnollFrank.lib.settingssearch.graph;

import androidx.preference.Preference;

import org.jgrapht.Graph;

import java.util.Map;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.common.Maps;

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
        this
                .getConnectedPreferenceScreenByPreference(root)
                .forEach(
                        (preference, child) -> {
                            buildPreferenceScreenGraph(child, preferenceScreenGraph);
                            preferenceScreenGraph.addVertex(child);
                            preferenceScreenGraph.addEdge(root, child, new PreferenceEdge(preference));
                        });
    }

    private Map<Preference, PreferenceScreenWithHost> getConnectedPreferenceScreenByPreference(final PreferenceScreenWithHost root) {
        return Maps.filter(
                connectedPreferenceScreenByPreferenceProvider.getConnectedPreferenceScreenByPreference(root),
                (final Preference preference, final PreferenceScreenWithHost child) ->
                        addEdgeToGraphPredicate.shallAddEdgeToGraph(
                                new PreferenceEdge(preference),
                                root,
                                child));
    }
}
