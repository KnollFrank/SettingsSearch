package de.KnollFrank.lib.settingssearch.graph;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostClass;
import de.KnollFrank.lib.settingssearch.common.graph.NodesTransformer;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClassFromPOJOConverter.PreferenceScreenWithHostClassWithMap;

public class MapFromEntityNodesRemover {

    public static Graph<PreferenceScreenWithHostClass, PreferenceEdge> removeMapFromEntityNodes(
            final Graph<PreferenceScreenWithHostClassWithMap, PreferenceEdge> pojoGraph) {
        return NodesTransformer.transformNodes(
                pojoGraph,
                MapFromEntityNodesRemover::removeMapFromEntityNode,
                PreferenceEdge.class,
                edge -> new PreferenceEdge(edge.preference));
    }

    private static PreferenceScreenWithHostClass removeMapFromEntityNode(final PreferenceScreenWithHostClassWithMap preferenceScreenWithHostClassPOJOWithMap) {
        return preferenceScreenWithHostClassPOJOWithMap.preferenceScreenWithHostClass();
    }
}
