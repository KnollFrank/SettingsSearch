package de.KnollFrank.lib.settingssearch.graph;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.common.graph.NodesTransformer;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClass2POJOConverter.PreferenceScreenWithHostClassPOJOWithMap;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJOEdge;

public class MapFromPojoNodesRemover {

    public static Graph<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> removeMapFromPojoNodes(
            final Graph<PreferenceScreenWithHostClassPOJOWithMap, SearchablePreferencePOJOEdge> pojoGraph) {
        return NodesTransformer.transformNodes(
                pojoGraph,
                MapFromPojoNodesRemover::removeMapFromPojoNode,
                SearchablePreferencePOJOEdge.class,
                edge -> new SearchablePreferencePOJOEdge(edge.preference));
    }

    public static PreferenceScreenWithHostClassPOJO removeMapFromPojoNode(final PreferenceScreenWithHostClassPOJOWithMap preferenceScreenWithHostClassPOJOWithMap) {
        return preferenceScreenWithHostClassPOJOWithMap.preferenceScreenWithHostClass();
    }
}
