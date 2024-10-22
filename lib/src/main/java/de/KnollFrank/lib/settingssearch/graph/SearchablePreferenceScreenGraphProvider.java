package de.KnollFrank.lib.settingssearch.graph;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostClass;

@FunctionalInterface
public interface SearchablePreferenceScreenGraphProvider {

    // FK-TODO: replace entityGraph with pojoGraph
    Graph<PreferenceScreenWithHostClass, PreferenceEdge> getSearchablePreferenceScreenGraph();
}
