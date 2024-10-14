package de.KnollFrank.lib.settingssearch.graph;

import androidx.preference.PreferenceScreen;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostClass;
import de.KnollFrank.lib.settingssearch.common.GraphTransformer;
import de.KnollFrank.lib.settingssearch.common.IGraphTransformer;

public class HostClassFromNodesRemover {

    public static Graph<PreferenceScreen, PreferenceEdge> removeHostClassFromNodes(
            final Graph<PreferenceScreenWithHostClass, PreferenceEdge> preferenceScreenGraph) {
        return GraphTransformer.transform(
                preferenceScreenGraph,
                PreferenceEdge.class,
                removeHostClassFromNodes());
    }

    private static IGraphTransformer<PreferenceScreenWithHostClass, PreferenceEdge, PreferenceScreen, PreferenceEdge> removeHostClassFromNodes() {
        return new IGraphTransformer<>() {

            @Override
            public PreferenceScreen transformNode(final PreferenceScreenWithHostClass preferenceScreenWithHost) {
                return removeHostClassFromNode(preferenceScreenWithHost);
            }

            private static PreferenceScreen removeHostClassFromNode(final PreferenceScreenWithHostClass preferenceScreenWithHost) {
                return preferenceScreenWithHost.preferenceScreen();
            }

            @Override
            public PreferenceEdge transformEdge(final PreferenceEdge edge, final PreferenceScreen transformedParentNode) {
                return new PreferenceEdge(edge.preference);
            }
        };
    }
}
