package de.KnollFrank.lib.settingssearch.graph;

import androidx.preference.Preference;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.common.graph.Edge;

@FunctionalInterface
public interface AddEdgeToTreePredicate {

    boolean shallAddEdgeToTree(Edge<PreferenceScreenWithHost, Preference> edge);
}
