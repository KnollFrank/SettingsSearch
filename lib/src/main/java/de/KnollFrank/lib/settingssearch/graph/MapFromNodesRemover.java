package de.KnollFrank.lib.settingssearch.graph;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.common.GraphTransformer;
import de.KnollFrank.lib.settingssearch.common.IGraphTransformer;

class MapFromNodesRemover {

    public static Graph<PreferenceScreenWithHost, PreferenceEdge> removeMapFromNodes(
            final Graph<SearchablePreferenceScreenWithMapAndHost, PreferenceEdge> preferenceScreenGraph) {
        return GraphTransformer.transform(
                preferenceScreenGraph,
                PreferenceEdge.class,
                createGraphTransformer());
    }

    private static IGraphTransformer<SearchablePreferenceScreenWithMapAndHost, PreferenceEdge, PreferenceScreenWithHost, PreferenceEdge> createGraphTransformer() {
        return new IGraphTransformer<>() {

            @Override
            public PreferenceScreenWithHost transformNode(final SearchablePreferenceScreenWithMapAndHost node) {
                return new PreferenceScreenWithHost(
                        node.searchablePreferenceScreenWithMap().searchablePreferenceScreen(),
                        node.host());
            }

            @Override
            public PreferenceEdge transformEdge(final PreferenceEdge edge, final PreferenceScreenWithHost transformedParentNode) {
                return new PreferenceEdge(edge.preference);
            }
        };
    }
}
