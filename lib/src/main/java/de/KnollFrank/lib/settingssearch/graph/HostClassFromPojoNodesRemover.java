package de.KnollFrank.lib.settingssearch.graph;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.common.graph.NodesTransformer;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithId;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

// FK-TODO: rename to IdFromSearchablePreferenceScreenWithIdRemover
public class HostClassFromPojoNodesRemover {

    // FK-TODO: rename to removeIdFromNodes()
    public static Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> removeHostClassFromNodes(
            final Graph<PreferenceScreenWithId, SearchablePreferenceEdge> pojoGraph) {
        return NodesTransformer.transformNodes(
                pojoGraph,
                HostClassFromPojoNodesRemover::removeHostClassFromNode,
                SearchablePreferenceEdge.class,
                edge -> new SearchablePreferenceEdge(edge.preference));
    }

    // FK-TODO: rename to removeIdFromNode()
    private static SearchablePreferenceScreen removeHostClassFromNode(final PreferenceScreenWithId node) {
        return node.preferenceScreen();
    }
}
