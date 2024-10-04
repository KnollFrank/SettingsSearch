package de.KnollFrank.lib.settingssearch.graph;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostClass;
import de.KnollFrank.lib.settingssearch.common.GraphTransformer;
import de.KnollFrank.lib.settingssearch.common.IGraphTransformer;

class Host2HostClassTransformer {

    public static Graph<PreferenceScreenWithHostClass, PreferenceEdge> transformHost2HostClass(
            final Graph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph) {
        return GraphTransformer.transform(
                preferenceScreenGraph,
                PreferenceEdge.class,
                createGraphTransformer());
    }

    private static IGraphTransformer<PreferenceScreenWithHost, PreferenceEdge, PreferenceScreenWithHostClass, PreferenceEdge> createGraphTransformer() {
        return new IGraphTransformer<>() {

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
