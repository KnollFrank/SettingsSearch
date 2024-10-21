package de.KnollFrank.lib.settingssearch.graph;

import androidx.preference.PreferenceScreen;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostClass;
import de.KnollFrank.lib.settingssearch.common.graph.NodesTransformer;

public class HostClassFromNodesRemover {

    public static Graph<PreferenceScreen, PreferenceEdge> removeHostClassFromNodes(
            final Graph<PreferenceScreenWithHostClass, PreferenceEdge> preferenceScreenGraph) {
        return NodesTransformer.transformNodes(
                preferenceScreenGraph,
                HostClassFromNodesRemover::removeHostClassFromNode,
                PreferenceEdge.class,
                preferenceEdge -> new PreferenceEdge(preferenceEdge.preference));
    }

    private static PreferenceScreen removeHostClassFromNode(final PreferenceScreenWithHostClass preferenceScreenWithHost) {
        return preferenceScreenWithHost.preferenceScreen();
    }
}
