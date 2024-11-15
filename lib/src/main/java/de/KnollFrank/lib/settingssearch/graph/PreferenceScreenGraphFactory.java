package de.KnollFrank.lib.settingssearch.graph;

import org.jgrapht.Graph;
import org.jgrapht.ListenableGraph;
import org.jgrapht.event.GraphVertexChangeEvent;
import org.jgrapht.event.VertexSetListener;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultListenableGraph;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;

class PreferenceScreenGraphFactory {

    public static Graph<PreferenceScreenWithHost, PreferenceEdge> createEmptyPreferenceScreenGraph(final PreferenceScreenGraphListener preferenceScreenGraphListener) {
        final ListenableGraph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph =
                new DefaultListenableGraph<>(
                        new DefaultDirectedGraph<>(
                                PreferenceEdge.class));
        preferenceScreenGraph.addVertexSetListener(getVertexSetListener(preferenceScreenGraphListener));
        return preferenceScreenGraph;
    }

    private static VertexSetListener<PreferenceScreenWithHost> getVertexSetListener(final PreferenceScreenGraphListener preferenceScreenGraphListener) {
        return new VertexSetListener<>() {

            @Override
            public void vertexAdded(final GraphVertexChangeEvent<PreferenceScreenWithHost> e) {
                preferenceScreenGraphListener.preferenceScreenWithHostAdded(e.getVertex());
            }

            @Override
            public void vertexRemoved(final GraphVertexChangeEvent<PreferenceScreenWithHost> e) {
            }
        };
    }
}
