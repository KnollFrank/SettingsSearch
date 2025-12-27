package de.KnollFrank.lib.settingssearch.graph;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;

@FunctionalInterface
public interface AddEdgeToGraphPredicate {

    boolean shallAddEdgeToGraph(PreferenceScreenWithHost sourceOfEdge, PreferenceScreenWithHost targetOfEdge, PreferenceEdge edge);
}
