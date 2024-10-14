package de.KnollFrank.lib.settingssearch.graph;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostClass;

@FunctionalInterface
public interface SearchablePreferenceScreenGraphProvider {

    Graph<PreferenceScreenWithHostClass, PreferenceEdge> getSearchablePreferenceScreenGraph();
}
