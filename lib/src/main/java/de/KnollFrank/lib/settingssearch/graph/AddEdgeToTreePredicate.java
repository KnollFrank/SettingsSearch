package de.KnollFrank.lib.settingssearch.graph;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;

@FunctionalInterface
public interface AddEdgeToTreePredicate {

    boolean shallAddEdgeToTree(
            // FK-TODO: use Preference instead of PreferenceEdge
            PreferenceEdge edge,
            PreferenceScreenWithHost sourceNodeOfEdge,
            PreferenceScreenWithHost targetNodeOfEdge);
}
