package de.KnollFrank.lib.settingssearch.graph;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostClass;
import de.KnollFrank.lib.settingssearch.common.graph.GraphTransformer;
import de.KnollFrank.lib.settingssearch.common.graph.GraphTransformerAlgorithm;

class Host2HostClassTransformer {

    public static Graph<PreferenceScreenWithHostClass, PreferenceEdge> transformHost2HostClass(
            final Graph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph) {
        return GraphTransformerAlgorithm.transform(
                preferenceScreenGraph,
                PreferenceEdge.class,
                createGraphTransformer());
    }

    private static GraphTransformer<PreferenceScreenWithHost, PreferenceEdge, PreferenceScreenWithHostClass, PreferenceEdge> createGraphTransformer() {
        return new GraphTransformer<>() {

            @Override
            public PreferenceScreenWithHostClass transformNode(final PreferenceScreenWithHost node) {
                return new PreferenceScreenWithHostClass(
                        node.preferenceScreen(),
                        node.host().getClass());
            }

            @Override
            public PreferenceEdge transformEdge(final PreferenceEdge edge, final PreferenceScreenWithHostClass transformedParentNode) {
                return new PreferenceEdge(edge.preference);
            }
        };
    }
}
