package de.KnollFrank.lib.settingssearch.graph;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.common.graph.NodesTransformer;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJOEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenPOJO;

public class HostClassFromPojoNodesRemover {

    public static Graph<SearchablePreferenceScreenPOJO, SearchablePreferencePOJOEdge> removeHostClassFromNodes(
            final Graph<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> pojoGraph) {
        return NodesTransformer.transformNodes(
                pojoGraph,
                HostClassFromPojoNodesRemover::removeHostClassFromNode,
                SearchablePreferencePOJOEdge.class,
                edge -> new SearchablePreferencePOJOEdge(edge.preference));
    }

    private static SearchablePreferenceScreenPOJO removeHostClassFromNode(final PreferenceScreenWithHostClassPOJO node) {
        return node.preferenceScreen();
    }
}
