package de.KnollFrank.lib.settingssearch.graph;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.common.graph.NodesTransformer;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClass2POJOConverter.PreferenceScreenWithHostClassPOJOWithMap;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJOEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenPOJO;

public class HostClassAndMapFromNodesRemover {

    public static Graph<SearchablePreferenceScreenPOJO, SearchablePreferencePOJOEdge> removeHostClassAndMapFromNodes(
            final Graph<PreferenceScreenWithHostClassPOJOWithMap, SearchablePreferencePOJOEdge> pojoGraph) {
        return NodesTransformer.transformNodes(
                pojoGraph,
                HostClassAndMapFromNodesRemover::removeHostClassAndMapFromNode,
                SearchablePreferencePOJOEdge.class,
                edge -> new SearchablePreferencePOJOEdge(edge.preference));
    }

    private static SearchablePreferenceScreenPOJO removeHostClassAndMapFromNode(final PreferenceScreenWithHostClassPOJOWithMap preferenceScreenWithHostClassPOJOWithMap) {
        return preferenceScreenWithHostClassPOJOWithMap.preferenceScreenWithHostClass().preferenceScreen();
    }
}
