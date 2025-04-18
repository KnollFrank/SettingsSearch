package de.KnollFrank.lib.settingssearch.graph;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.common.graph.NodesTransformer;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClass2POJOConverter.PreferenceScreenWithHostClassWithMap;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

public class MapFromPojoNodesRemover {

    public static Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> removeMapFromPojoNodes(
            final Graph<PreferenceScreenWithHostClassWithMap, SearchablePreferenceEdge> pojoGraph) {
        return NodesTransformer.transformNodes(
                pojoGraph,
                MapFromPojoNodesRemover::removeMapFromPojoNode,
                SearchablePreferenceEdge.class,
                edge -> new SearchablePreferenceEdge(edge.preference));
    }

    public static SearchablePreferenceScreen removeMapFromPojoNode(final PreferenceScreenWithHostClassWithMap preferenceScreenWithHostClassWithMap) {
        return preferenceScreenWithHostClassWithMap.searchablePreferenceScreen();
    }
}
