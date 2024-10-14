package de.KnollFrank.lib.settingssearch.graph;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.common.graph.NodesTransformer;

class MapFromNodesRemover {

    public static Graph<PreferenceScreenWithHost, PreferenceEdge> removeMapFromNodes(
            final Graph<SearchablePreferenceScreenWithMapAndHost, PreferenceEdge> preferenceScreenGraph) {
        return NodesTransformer.transformNodes(
                preferenceScreenGraph,
                MapFromNodesRemover::removeMapFromNode);
    }

    private static PreferenceScreenWithHost removeMapFromNode(final SearchablePreferenceScreenWithMapAndHost node) {
        return new PreferenceScreenWithHost(
                node.searchablePreferenceScreenWithMap().searchablePreferenceScreen(),
                node.host());
    }
}
