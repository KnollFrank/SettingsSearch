package de.KnollFrank.lib.settingssearch.graph;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.common.graph.NodesTransformer;
import de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceScreenWithMap;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;

public class MapFromPojoNodesRemover {

    public static Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEdge> removeMapFromPojoNodes(
            final Graph<SearchablePreferenceScreenWithMap, SearchablePreferenceEdge> pojoGraph) {
        return NodesTransformer.transformNodes(
                pojoGraph,
                MapFromPojoNodesRemover::removeMapFromPojoNode,
                SearchablePreferenceEdge.class,
                edge -> new SearchablePreferenceEdge(edge.preference));
    }

    public static SearchablePreferenceScreenEntity removeMapFromPojoNode(final SearchablePreferenceScreenWithMap searchablePreferenceScreenWithMap) {
        return searchablePreferenceScreenWithMap.searchablePreferenceScreen();
    }
}
