package de.KnollFrank.lib.settingssearch.graph;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostClass;
import de.KnollFrank.lib.settingssearch.common.graph.NodesTransformer;

class Host2HostClassTransformer {

    public static Graph<PreferenceScreenWithHostClass, PreferenceEdge> transformHost2HostClass(
            final Graph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph) {
        return NodesTransformer.transformNodes(
                preferenceScreenGraph,
                preferenceScreenWithHost -> new PreferenceScreenWithHostClass(
                        preferenceScreenWithHost.preferenceScreen(),
                        preferenceScreenWithHost.host().getClass()));
    }
}
