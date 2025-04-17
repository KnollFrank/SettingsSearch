package de.KnollFrank.lib.settingssearch.graph;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.common.graph.NodesTransformer;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClass2POJOConverter.PreferenceScreenWithHostClassWithMap;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithId;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;

public class MapFromPojoNodesRemover {

    public static Graph<PreferenceScreenWithId, SearchablePreferenceEdge> removeMapFromPojoNodes(
            final Graph<PreferenceScreenWithHostClassWithMap, SearchablePreferenceEdge> pojoGraph) {
        return NodesTransformer.transformNodes(
                pojoGraph,
                MapFromPojoNodesRemover::removeMapFromPojoNode,
                SearchablePreferenceEdge.class,
                edge -> new SearchablePreferenceEdge(edge.preference));
    }

    public static PreferenceScreenWithId removeMapFromPojoNode(final PreferenceScreenWithHostClassWithMap preferenceScreenWithHostClassWithMap) {
        return preferenceScreenWithHostClassWithMap.preferenceScreenWithId();
    }
}
