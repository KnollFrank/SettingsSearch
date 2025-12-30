package de.KnollFrank.lib.settingssearch.common.graph;

import org.jgrapht.graph.DefaultDirectedGraph;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

public class SearchablePreferenceScreenSubtreeReplacerFactory {

    public static SubtreeReplacer<SearchablePreferenceScreen, SearchablePreferenceEdge> createSubtreeReplacer() {
        return new SubtreeReplacer<>(
                () -> new DefaultDirectedGraph<>(SearchablePreferenceEdge.class),
                edge -> new SearchablePreferenceEdge(edge.preference));
    }
}
