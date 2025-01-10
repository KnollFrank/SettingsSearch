package de.KnollFrank.lib.settingssearch.graph;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.common.graph.NodesTransformer;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClass;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

public class HostClassFromPojoNodesRemover {

    public static Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> removeHostClassFromNodes(
            final Graph<PreferenceScreenWithHostClass, SearchablePreferenceEdge> pojoGraph) {
        return NodesTransformer.transformNodes(
                pojoGraph,
                HostClassFromPojoNodesRemover::removeHostClassFromNode,
                SearchablePreferenceEdge.class,
                edge -> new SearchablePreferenceEdge(edge.preference));
    }

    private static SearchablePreferenceScreen removeHostClassFromNode(final PreferenceScreenWithHostClass node) {
        return node.preferenceScreen();
    }
}
