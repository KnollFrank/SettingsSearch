package de.KnollFrank.lib.settingssearch.graph;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.common.graph.NodesTransformer;
import de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceScreenWithMap;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntityEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;

public class MapFromPojoNodesRemover {

    public static Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> removeMapFromPojoNodes(
            final Graph<SearchablePreferenceScreenWithMap, SearchablePreferenceEntityEdge> pojoGraph) {
        return NodesTransformer.transformNodes(
                pojoGraph,
                MapFromPojoNodesRemover::removeMapFromPojoNode,
                SearchablePreferenceEntityEdge.class,
                edge -> new SearchablePreferenceEntityEdge(edge.preference));
    }

    public static SearchablePreferenceScreenEntity removeMapFromPojoNode(final SearchablePreferenceScreenWithMap searchablePreferenceScreenWithMap) {
        return searchablePreferenceScreenWithMap.searchablePreferenceScreen();
    }
}
